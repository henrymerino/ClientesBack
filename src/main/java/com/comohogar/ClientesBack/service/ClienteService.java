package com.comohogar.ClientesBack.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comohogar.ClientesBack.model.Cliente;
import com.comohogar.ClientesBack.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service("clienteService")
@Transactional
public class ClienteService {

	private final ClienteRepository clienteRepository;

	@Autowired
	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public Cliente addCliente(Cliente cliente) {
		Cliente cl = null;
		List<Cliente> ls = null;
		List<String> lsBen = null;
		cl = validaDatos(cliente);

		if ("00".equals(cl.getCodMensaje())) {
			cl = findClienteByName(cliente.getName());
			if ("00".equals(cl.getCodMensaje())) {
				ls = findClienteByNameGrupo(cliente.getGrupo().toUpperCase());
				lsBen = new ArrayList<>();
				for (Cliente c : ls) {
					lsBen.add(c.getBeneficio());
				}
				cl.setBeneficio(obtenerBeneficio(lsBen, cliente.getGrupo()));
				if (cl.getBeneficio() != null) {
					cliente.setBeneficio(cl.getBeneficio());
					cl = clienteRepository.save(cliente);
				} else {
					cl = new Cliente("Estimado usuario no existe más beneficios", "02");
				}

			}
		}
		return cl;
	}

	public List<Cliente> findClientes() {
		return clienteRepository.findAll();
	}

	public List<Cliente> findClienteByNameGrupo(String grupo){
		return clienteRepository.findClienteByNameGrupo(grupo);
	}
	
	public Cliente findClienteByName(String name) {
		Cliente cl = null;
		List<Cliente> ls = null;
		ls = clienteRepository.findClienteByName(name);
		if (ls.size() == 0) {
			cl = new Cliente("Usuario nuevo", "00");
		} else if (ls.size() > 0) {
			cl = new Cliente("Usuario ya existe", "01");
		}
		return cl;
	}

	public String obtenerBeneficio(List<String> ls, String grupo) {
		String beneficio = null;
		if ("SK".equals(grupo.toUpperCase())) {
			beneficio = validarBeneficioSk(ls);
		} else if ("TH".equals(grupo.toUpperCase())) {
			beneficio = validarBeneficioTh(ls);
		}

		return beneficio;
	}

	private String validarBeneficioSk(List<String> lsBen) {
		String beneficio = null;
		List<String> ls = null;
		Object ob;
		try {
			ob = new JSONParser().parse(new FileReader("Referencias/sk_formato.json"));
			org.json.simple.JSONObject js = (org.json.simple.JSONObject) ob;
			org.json.simple.JSONArray js2 = (JSONArray) js.get("sk_formato");
			ls = new ArrayList<>();
			for (int i = 0; i < js2.size(); i++) {
				org.json.simple.JSONObject jsr = (JSONObject) js2.get(i);
				ls.add((String) jsr.get("beneficio"));
			}

			if (lsBen != null && lsBen.size() > 0 && !lsBen.equals(ls)) {
				for (String item : ls) {
					if (!lsBen.contains(item)) {
						beneficio = item;
						break;
					}
				}

			} else if (lsBen != null && lsBen.size() > 0 && lsBen.equals(ls)) {
				beneficio = null;
			} else if (lsBen.size() == 0) {
				beneficio = ls.get(0);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beneficio;
	}

	private String validarBeneficioTh(List<String> lsBen) {
		String beneficio = null;
		List<String> ls = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document documento = builder.parse(new File("Referencias/th_formato.xml"));
			documento.getDocumentElement().normalize();
			ls = new ArrayList<>();
			NodeList nodoBeneficios = documento.getElementsByTagName("beneficio");
			for (int temp = 0; temp < nodoBeneficios.getLength(); temp++) {
				Node nNode = nodoBeneficios.item(temp);
				Element eElement = (Element) nNode;
				ls.add(eElement.getTextContent());
			}

			if (lsBen != null && lsBen.size() > 0 && !lsBen.equals(ls)) {
				for (String item : ls) {
					if (!lsBen.contains(item)) {
						beneficio = item;
						break;
					}
				}

			} else if (lsBen != null && lsBen.size() > 0 && lsBen.equals(ls)) {
				beneficio = null;
			} else if (lsBen.size() == 0) {
				beneficio = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beneficio;
	}

	public Cliente validaDatos(Cliente cl) {
		Cliente cli = new Cliente("Datos", "00");
		if (cl.getName() == null || cl.getName().isEmpty()) {
			return new Cliente("Dato requerido: name", "11");
		}

		if (cl.getGrupo() == null || cl.getGrupo().isEmpty()) {
			return new Cliente("Dato requerido: grupo", "12");
		}

		if (cl.getEmail() == null || cl.getEmail().isEmpty()) {
			return new Cliente("Dato requerido: email", "13");
		}

		if (cl.getPhone() == null || cl.getPhone().isEmpty()) {
			return new Cliente("Dato requerido: phone", "14");
		}

		if (cl.getStore() == null || cl.getEmail().isEmpty()) {
			return new Cliente("Dato requerido: store", "15");
		}

		return cli;
	}
}
