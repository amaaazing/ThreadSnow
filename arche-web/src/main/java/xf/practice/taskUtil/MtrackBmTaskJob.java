package xf.practice.taskUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MtrackBmTaskJob extends ParseTaskJob {

    public MtrackBmTaskJob(TaskContext context, SamClubDataImportTaskService hedwigService) {
        super(context, hedwigService);
    }

    public List<SamClubHostLog> read(BufferedReader reader) throws Exception {
        List<SamClubHostLog> content = new ArrayList<SamClubHostLog>();
        String line;

        int index = 1;
        while ((line = reader.readLine()) != null) {

            if (index > 1000) {
                throw new Exception("单个文件请不要超过1000条记录");
            }
            try {
                String[] s = line.split("::");

                SamClubHostLog hostLog = new SamClubHostLog();

                for (int i = 0; i < s.length; i++) {
                    if (!s[i].contains(":") && !s[i].contains("NOT")) {
                        s[i] = s[i].replaceAll("\\s", "");
                    }
                    if (StringUtils.isBlank(s[i])) {
                        s[i] = null;
                    }
                }

                Date now = new Date();

                if (StringUtils.isNumeric(s[0])) {
//                    hostLog.setIssuingClubNbrH(Integer.parseInt(s[0]));
                }
//                hostLog.setMemberCode(s[1]);

                if (StringUtils.isNumeric(s[2])) {
//                    hostLog.setSicCode(Integer.parseInt(s[2]));
                }
//                hostLog.setSicCodeDescr(s[3]);
//                hostLog.setCurrStatusCode(s[4]);
//                hostLog.setMembershipNbr(s[5]);
//                hostLog.setCardHolderNbr(s[6]);
//                hostLog.setCardStatCd(s[7]);
//                hostLog.setChTypeShortDesc(s[8]);
//                hostLog.setFullName(s[9]);
//                hostLog.setPassportNbr(s[10]);
//                hostLog.setPhoneNbr(s[11]);
//                hostLog.setPhoneNbr2(s[12]);
//                hostLog.setPostalCode(s[13]);
//                hostLog.setAddressLine1(s[14]);
//                hostLog.setAddressLine2(s[15]);
//                hostLog.setAddressLine3(s[16]);

                if (StringUtils.isNumeric(s[17])) {
//                    hostLog.setMytdPurchaseAmt(Integer.valueOf(s[17]));
                }

                if (StringUtils.isNumeric(s[18])) {
//                    hostLog.setMytdVisitCnt(Integer.valueOf(s[18]));
                }
                if (StringUtils.isNumeric(s[19])) {
//                    hostLog.setFytdPurchaseAmt(Integer.valueOf(s[19]));
                }

                if (StringUtils.isNumeric(s[20])) {
//                    hostLog.setFytdVisitCnt(Integer.valueOf(s[20]));
                }
//                hostLog.setCtzidNbr(s[21]);

                try {
//                    hostLog.setStartDate(super.dateformat.parse(s[22]));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[StartDate不为yyyy-MM-dd格式]");
                }

                try {
//                    hostLog.setNextRenewDate(super.dateformat.parse(s[23]));
                } catch (Exception e) {
                    this.logger.error("-------第" + index + "行NextrenewDate报错-------", e);
                }

                try {
//                    hostLog.setBirthDate(super.dateformat.parse(s[24]));
                } catch (Exception e) {
                    this.logger.error("-------第" + index + "行BirthDate报错-------", e);
                }

//                hostLog.setCompPostCode(s[25]);
//                hostLog.setCompAddr1(s[26]);
//                hostLog.setCompAddr2(s[27]);
//                hostLog.setCompTelNbr(s[28]);
//                hostLog.setCompFaxNbr(s[29]);
//                hostLog.setCompanyName(s[30]);
//                hostLog.setCompanyCard(s[31]);
//                hostLog.setDriverNbr(s[32]);
//                hostLog.setInfoCompleteInd(s[33]);
//                hostLog.setEmailAddr(s[34]);
//                hostLog.setReceivedMail(s.length >= 36 ? s[35] : null);
//                hostLog.setAcceptPhone(s.length >= 37 ? s[36] : null);

                try {
                    if (s.length >= 38) {
//                        hostLog.setLastShopDate(super.dateformat.parse(s[37]));
                    }
                } catch (Exception e) {
                    this.logger.error("-------第" + index + "行LastShopDate报错-------", e);
                }
//                hostLog.setCreateTime(now);
//                hostLog.setUpdateTime(now);
                try {
//                    hostLog.setSamCardNo(combineSamCardNo(hostLog.getMemberCode(), hostLog.getCardHolderNbr(), hostLog.getMembershipNbr()));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[" + e.getMessage() + "]");
                }
                content.add(hostLog);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new Exception("第" + index + "行数据解析出错,原因[列数不正确," + e.getMessage() + "]");
            }

            index++;
        }
        return content;
    }
}

