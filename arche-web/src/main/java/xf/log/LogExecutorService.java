package xf.log;

public interface LogExecutorService {

    public void executorDBSql(final LogSqlVo logSqlVo);

    public void executorActionSql(final BackendLog grouponLog);
    
    public void executorLog(final RecordLog grouponRecordLog);
}
