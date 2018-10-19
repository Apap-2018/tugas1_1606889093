package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.repository.InstansiDb;

@Service
@Transactional
public class InstansiServiceImpl implements InstansiService {
	@Autowired
	private InstansiDb instansiDb;
	

	@Override
	public InstansiModel getInstansiById(long id) {
		// TODO Auto-generated method stub
		return instansiDb.getOne(id);
	}
	
	@Override
	public List<InstansiModel> getInstansiList(){
		return instansiDb.findAll();
	}
}
