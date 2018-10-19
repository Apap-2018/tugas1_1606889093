package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;


@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {
	
	@Autowired
	private JabatanDb jabatanDb;

	@Override
	public void addJabatan(JabatanModel jabatan) {
		// TODO Auto-generated method stub
		jabatanDb.save(jabatan);
		}

	@Override
	public JabatanModel findJabatanById(long id) {
		// TODO Auto-generated method stub
		return jabatanDb.getOne(id);
	}
	
	@Override
	public List<JabatanModel> getJabatanList(){
		return jabatanDb.findAll();
	}

	//@Override
	//public Optional<JabatanModel> findJabatanDetailByID(Long id) {
		// TODO Auto-generated method stub
		//return jabatanDb.findById(id);
	//}
	
	
}	
