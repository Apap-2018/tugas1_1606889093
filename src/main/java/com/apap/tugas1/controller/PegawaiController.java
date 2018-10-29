package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("jabatanList", jabatanService.getJabatanList());
		model.addAttribute("instansiList", instansiService.getInstansiList());
		return "home";
	}
	
	@RequestMapping("/pegawai")
	private String viewDetailPegawai(@RequestParam("nip") String nip, Model model) {
		double gaji = 0;
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		
		for(JabatanModel jabatan : pegawai.getJabatanList()) {
			double gajiDummy = jabatan.getGajiPokok();
			if(gaji < gajiDummy) {
				gaji = gajiDummy;
			}
		}
		
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		double gajiTotal = gaji + ((tunjangan/100) * gaji);
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gajiPegawai", gajiTotal);
		return "data-pegawai";
		
	}
	
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewTuaMuda(@RequestParam("idInstansi") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		
		PegawaiModel pegawaiTermuda;
		PegawaiModel pegawaiTertua;
		
		if(listPegawai.size() > 0) {
			pegawaiTermuda = listPegawai.get(0);
			pegawaiTertua = listPegawai.get(0);
			
			for(PegawaiModel pegawai : listPegawai) {
				Date tanggalLahir = pegawai.getTanggalLahir();
				if(tanggalLahir.after(pegawaiTermuda.getTanggalLahir())) {
					pegawaiTermuda = pegawai;
				}
				if(tanggalLahir.before(pegawaiTertua.getTanggalLahir())) {
					pegawaiTertua = pegawai;
				}
			}
			model.addAttribute("pegawaiTermuda", pegawaiTermuda);
			model.addAttribute("pegawaiTertua", pegawaiTertua);
		}
		
		
		return "view-tertua-termuda";
		
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		model.addAttribute("pegawai", new PegawaiModel());
		pegawai.setInstansi(new InstansiModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("instansiList", instansiService.getInstansiList());
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		return "tambah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
		private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
			String NIP = ""; 
			NIP += pegawai.getInstansi().getId();
		
			String[]tanggalLahir = pegawai.getTanggalLahir().toString().split("-");
			String tanggalLahirStr = tanggalLahir[2] + tanggalLahir[1] + tanggalLahir[0].substring(2, 4);
			
			NIP += tanggalLahirStr;
			NIP += pegawai.getTahunMasuk();
		
			int counter = 1;
			for(PegawaiModel instansiPegawai:pegawai.getInstansi().getPegawaiInstansi()) {
				if(instansiPegawai.getTahunMasuk().equals(pegawai.getTahunMasuk()) && instansiPegawai
						.equals(pegawai.getTanggalLahir())) {
					counter += 1;
				}
			}
			
			NIP += "0" + counter;
			pegawai.setNip(NIP);
			
			pegawaiService.addPegawai(pegawai);
			model.addAttribute("pegawai", pegawai);
			return "berhasil-tambah-pegawai";
		}
	
	@RequestMapping(value = "/pegawai/ubah")
	private String ubahPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("instansiList", instansiService.getInstansiList());
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listJabatan", jabatanService.getJabatanList());
		return "ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	private String ubahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		
		String NIP = "" + pegawai.getInstansi().getId();
		
		String[] arrTanggalLahir = pegawai.getTanggalLahir().toString().split("-");
		String strTanggalLahir = arrTanggalLahir[2] + arrTanggalLahir[1] + arrTanggalLahir[0].substring(2, 4);
		NIP += strTanggalLahir;
		
		NIP += pegawai.getTahunMasuk();
		
		int counter = 1;
		
		for(PegawaiModel pegawaiInstansi : pegawai.getInstansi().getPegawaiInstansi()) {
			if(pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())
					&& pegawaiInstansi.getId() != pegawai.getId()) {
				counter++;
			}
			
		}
		NIP += "0" + counter;
		
		pegawai.setNip(NIP);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pegawai", pegawai);
		
		return "berhasil-ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String cariPegawai(@RequestParam(value="idProvinsi", required = false) String idProvinsi,
			@RequestParam(value ="idInstansi", required = false) String idInstansi,
			@RequestParam(value = "idJabatan", required = false) String idJabatan,
			Model model) {
		
		List<PegawaiModel> listPegawai = pegawaiService.getListPegawai();
		model.addAttribute("listInstansi", instansiService.getInstansiList());
		model.addAttribute("listJabatan", jabatanService.getJabatanList());
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
			return "cari-pegawai";
		}
		else {
			
			// filter untuk pegawai yg bekerja di provinsi terkait
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			
			// filter untuk pegawai yg bekerja di instansi terkait
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				listPegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			
			// filter untuk pegawai yg menjabat jabatan terkait
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: listPegawai) {
					for (JabatanModel jabatan:peg.getJabatanList()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				listPegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",listPegawai);
		return "cari-pegawai";
	}
	
}
