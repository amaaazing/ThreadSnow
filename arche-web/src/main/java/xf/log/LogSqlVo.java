package xf.log;

import java.util.List;

import org.apache.ibatis.mapping.ParameterMapping;

public class LogSqlVo {

    private String sql; // 具体sql
    private ParameterMapping[] paramMappSet;
    private Object id; // 数据的id
    private Object parameterObject; // 方法的参数值
    private String uuid;
    private List<Long> idList;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object parameterObject) {
        this.parameterObject = parameterObject;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public ParameterMapping[] getParamMappSet() {
        return paramMappSet;
    }

    public void setParamMappSet(ParameterMapping[] paramMappSet) {
        this.paramMappSet = paramMappSet;
    }

}
