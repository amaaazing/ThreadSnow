package xf.log;

import java.util.Date;

public class BackendLog {

    private Long id;
    private String uuid;
    private String comment;
    private String operateData;
    private Date operateTime;
    private Integer operateType;
    private Integer bussType;
    private Integer secBussType;
    private Long userId;
    private String methodName;
    private String clientIp;
    private String serverIp;
    private Date createTime;
    private String userName;
    private String whereValue;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOperateData() {
		return operateData;
	}
	public void setOperateData(String operateData) {
		this.operateData = operateData;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Integer getBussType() {
		return bussType;
	}
	public void setBussType(Integer bussType) {
		this.bussType = bussType;
	}
	public Integer getSecBussType() {
		return secBussType;
	}
	public void setSecBussType(Integer secBussType) {
		this.secBussType = secBussType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWhereValue() {
		return whereValue;
	}
	public void setWhereValue(String whereValue) {
		this.whereValue = whereValue;
	}
   
    
    
}
