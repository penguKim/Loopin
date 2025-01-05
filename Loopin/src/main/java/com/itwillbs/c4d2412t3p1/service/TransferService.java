package com.itwillbs.c4d2412t3p1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.entity.Transfer;
import com.itwillbs.c4d2412t3p1.repository.TransferRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class TransferService {

	private final TransferRepository transferRepository;

	public List<Transfer> findAll() {

		return transferRepository.findAll();
	}

	public void insert_TRANSFER(TransferDTO transferDTO) {

		Transfer transfer = Transfer.setTransferEntity(transferDTO);

		transfer.setTransfer_aw("대기");
		transfer.setTransfer_wd("1");
		transfer.setTransfer_wr("1");
		transfer.setTransfer_md("1");
		transfer.setTransfer_mf("1");

		transferRepository.save(transfer);

	}

	public void delete_TRANSFER(List<Long> ids) {
		
		transferRepository.deleteAllById(ids);

	}
}
