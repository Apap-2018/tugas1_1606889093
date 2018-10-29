package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel findPegawaiByNip(String nip);

	void addPegawai(PegawaiModel pegawai);

	List<PegawaiModel> getListPegawai();
}
