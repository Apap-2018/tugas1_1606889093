package com.apap.tugas1.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	
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
}
