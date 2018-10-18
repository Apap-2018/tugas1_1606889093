package com.apap.tugas1.service;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	JabatanModel findJabatanById(long id);
}
