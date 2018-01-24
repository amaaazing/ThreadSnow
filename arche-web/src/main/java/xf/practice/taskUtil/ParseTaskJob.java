package xf.practice.taskUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @description  解析文件TaskJob
 */
public abstract class ParseTaskJob extends TaskJob {

    public ParseTaskJob(TaskContext context, SamClubDataImportTaskService hedwigService) {
        super(context, hedwigService);
    }

    //所有的时间字段都为此格式
    protected SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void dotask() throws Exception {

        /*
         * 保存至文件系统
         */
        save2fs(temFile);

        /*
         * 将文件内容读取至内存
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.temFile), Charset.forName("gb2312")));
        List<SamClubHostLog> content = read(reader);

        /*
         * 保存至数据库
         */
        save2db(content);

    }

    private void save2db(List<SamClubHostLog> content) throws Exception {

//        Return<Boolean> ret = this.hedwigService.batchInsertHostLog(new SamDataImportHostLogInsertIn(content));
//        if (!ret.isSuccess() || ret.getCode() == RetDict.CODE_ERROR) {
//            throw new Exception(ret.getMessage());
//        }

        saveTask();
    }

    /**
     *
     * @description 解析TXT文件
     * @param reader
     * @return
     * @throws Exception
     */
    public abstract List<SamClubHostLog> read(BufferedReader reader) throws Exception;

    /**
     *
     * @description 保存文件到YDFS 如果保存失败 则当前TaskJob任务失败
     * @param temFile
     * @throws Exception
     */
    private void save2fs(File temFile) throws Exception {
        try {
            super.downurl = SamDataFileUtils.upload2Ydfs(temFile, taskId);
        } catch (Exception e) {
            throw new Exception("上传文件失败:" + e.getMessage());
        }
    }

    /**
     *
     * @description sam卡号生成规则
     * @param memberCode
     * @param cardHolderNbr
     * @param memberShipNbr
     * @return
     * @throws Exception
     */
    protected String combineSamCardNo(String memberCode, String cardHolderNbr, String memberShipNbr) throws Exception {
        if (StringUtils.isEmpty(cardHolderNbr) || StringUtils.isEmpty(memberShipNbr) || StringUtils.isEmpty(memberCode)) {
            throw new Exception("必要参数不允许为空");
        }
        if (!NumberUtils.isDigits(cardHolderNbr) || !NumberUtils.isDigits(memberShipNbr)) {
            throw new Exception("cardHolderNbr或者memberShipNbr必须为数字");
        }
        String chn = lpad(2, Integer.valueOf(cardHolderNbr));
        String msn = lpad(9, Integer.valueOf(memberShipNbr));

        if (SamClubConst.MEMBER_CODE_PERSONAL.equals(memberCode)) {
            return "10742".concat(chn).concat("0").concat(msn);
        } else if (SamClubConst.MEMBER_CODE_COMMERCIAL.equals(memberCode)) {
            return "10734".concat(chn).concat("0").concat(msn);
        } else {
            throw new Exception("Member code不符合规则");
        }
    }

    private void saveTask() {
//        SamDataImportInsertIn insertIn = new SamDataImportInsertIn();
//        insertIn.setEndUserId(endUserId);
//        insertIn.setFilename(filename.toString());
//        insertIn.setRealname(realname);
//        insertIn.setTaskId(taskId);
//        insertIn.setDownurl(downurl);
//        this.hedwigService.insertImportTask(insertIn);
    }

    /**
     *
     * @Title: lpad
     * @Description: 不足length长度的左边补0
     * @return: String
     * @throws
     */
    private String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
}

