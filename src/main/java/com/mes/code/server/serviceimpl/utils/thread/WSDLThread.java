package com.mes.code.server.serviceimpl.utils.thread;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.code.server.service.FMCService;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.serviceimpl.FMCServiceImpl;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

@Component
public class WSDLThread implements DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(WSDLThread.class);

	private static WSDLThread Instance;

	@Autowired
	FMCService wRSMService = new FMCServiceImpl();

	@PostConstruct
	public void init() {
		Instance = this;
		Instance.AdminUser = this.AdminUser;
		Run();
		// 初使化时将已静态化的testService实例化
	}

	private BMSEmployee AdminUser = new BMSEmployee();

	public WSDLThread() {
		super();
		AdminUser = BaseDAO.SysAdmin;
	}

	boolean mIsStart = false;

	private void Run() {
		try {
			if (mIsStart)
				return;
			mIsStart = true;
			logger.info("WSDL Start!!");

			Thread.sleep(10000);
			new Thread(() -> {
				while (mIsStart) {
					try {
//						this.WSDLBiz();

						// ①自动保存标准BOM
//						Instance.wRSMService.MES_AutoSaveBOM(AdminUser);

						Thread.sleep(3000);
					} catch (Exception ex) {
						logger.error(ex.toString());
					}
				}
			}).start();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 线程升版本
	 */
//	private synchronized void WSDLBiz() {
//		try {
//			if (WsdlConstants.mThreadOPList != null && WsdlConstants.mThreadOPList.size() > 0) {
//				List<ThreadOP> wDeleteList = new ArrayList<ThreadOP>();
//				for (ThreadOP wThreadOP : WsdlConstants.mThreadOPList) {
//					BOPDAO.getInstance().AddVersion(wThreadOP);
//					wDeleteList.add(wThreadOP);
//				}
//				WsdlConstants.mThreadOPList.removeAll(wDeleteList);
//			}
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//	}

	@Override
	public void destroy() throws Exception {
		try {
			mIsStart = false;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
