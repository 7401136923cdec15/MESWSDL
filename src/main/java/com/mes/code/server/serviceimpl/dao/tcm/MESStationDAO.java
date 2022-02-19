package com.mes.code.server.serviceimpl.dao.tcm;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.code.server.service.po.tcm.MESStation;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class MESStationDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(MESStationDAO.class);

	private static MESStationDAO Instance = null;

	private MESStationDAO() {
		super();
	}

	public static MESStationDAO getInstance() {
		if (Instance == null)
			Instance = new MESStationDAO();
		return Instance;
	}

	/**
	 * 将xml字符串解析成工位数据
	 */
	@SuppressWarnings("unchecked")
	public List<MESStation> GetMESStationList(String wXmlStr) {
		List<MESStation> wResult = new ArrayList<MESStation>();
		try {
			if (StringUtils.isEmpty(wXmlStr)) {
				return wResult;
			}

			Document wDocument = DocumentHelper.parseText(wXmlStr);

			List<Node> wItemNodes = wDocument.selectNodes("//ITEM");
			for (Node wNode : wItemNodes) {
				Node wMEStationIDNode = wNode.selectSingleNode("MEStationID");
				String wStationCode = wMEStationIDNode.getText();

				Node wMEStationNameCode = wNode.selectSingleNode("MEStationName");
				String wMEStationName = wMEStationNameCode.getText();

				MESStation wMESStation = new MESStation();
				wMESStation.setMEStationID(wStationCode);
				wMESStation.setMEStationName(wMEStationName);
				wResult.add(wMESStation);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
