package kr.co.seoulit.logistics.communitysvc.service;

import kr.co.seoulit.logistics.communitysvc.to.NewBoardTO;
import kr.co.seoulit.logistics.communitysvc.mapper.NewBoardMapper;
import kr.co.seoulit.logistics.purcstosvc.stock.mapper.BomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NewBoardServiceImpl implements NewBoardService {

	@Autowired
	private BomMapper bomMapper;
	@Autowired
	private NewBoardMapper newBoardMapper;

	@Override
	public ArrayList<NewBoardTO> findNewBoardList() {

		ArrayList<NewBoardTO> boardList = null;
		System.out.println("여기");
		boardList = newBoardMapper.selectNewBoardList();
		System.out.println(boardList+"저기");
		return boardList;
	}
	@Override
	public void registerNewBoard(NewBoardTO newBoard) {

		newBoardMapper.insertNewBoard(newBoard);
	}
	@Override
	public NewBoardTO findNewBoardDetail(int id){

		NewBoardTO boardDetail = null;
		boardDetail = newBoardMapper.selectNewBoardDetail(id);
		System.out.println("boardDetail"+boardDetail);
		return boardDetail;
	}
	@Override
	public void removeNewBoard(int id) {

		newBoardMapper.deleteNewBoard(id);
	}
	@Override
	public void modifyNewBoard(NewBoardTO newBoard){

		newBoardMapper.updateNewBoard(newBoard);
		System.out.println("newBoard"+newBoard);
	}
}
