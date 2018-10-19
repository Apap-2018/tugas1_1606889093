package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	JabatanModel findJabatanById(long id);
	List<JabatanModel> getJabatanList();
	//Optional<JabatanModel> findJabatanDetailByID(Long id);
	void deleteJabatanById(long idJabatan);
}
