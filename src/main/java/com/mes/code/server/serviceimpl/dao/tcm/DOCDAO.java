package com.mes.code.server.serviceimpl.dao.tcm;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.code.server.service.po.tcm.DOC;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class DOCDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(DOCDAO.class);

	private static DOCDAO Instance = null;

	private DOCDAO() {
		super();
	}

	public static DOCDAO getInstance() {
		if (Instance == null)
			Instance = new DOCDAO();
		return Instance;
	}

	/**
	 * 将xml字符串解析成BOP数据
	 */
	@SuppressWarnings("unchecked")
	public List<DOC> GetDOCList(String wXmlStr) {
		List<DOC> wResult = new ArrayList<DOC>();
		try {
			Document wDocument = DocumentHelper.parseText(wXmlStr);

			Node wDOCLISTNode = wDocument.selectSingleNode("//DOCLIST");
			List<Node> wDOCNodes = wDOCLISTNode.selectNodes("DOC");
			for (Node wDOCNode : wDOCNodes) {
				DOC wDOC = new DOC();
				wDOC.setDOCID(wDOCNode.selectSingleNode("DOCID").getText());
				wDOC.setDOCREV(wDOCNode.selectSingleNode("DOCREV").getText());
				wDOC.setDOCNAME(wDOCNode.selectSingleNode("DOCNAME").getText());
				wResult.add(wDOC);
				// ①解析DOCDATELIST
				List<String> wDOCDATELIST = new ArrayList<String>();
				wDOC.setDOCDATELIST(wDOCDATELIST);
				Node wDOCDATELISTNode = wDOCNode.selectSingleNode("DOCDATELIST");
				List<Node> wDOCDATENodes = wDOCDATELISTNode.selectNodes("DOCDATE");
				for (Node wDOCDATENode : wDOCDATENodes) {
					String wItem = wDOCDATENode.getText();
					if (StringUtils.isNotEmpty(wItem)) {
						wDOCDATELIST.add(wItem);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
