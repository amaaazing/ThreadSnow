package xf.practice.taskUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MtrackAdvTaskJob extends ParseTaskJob {

    public MtrackAdvTaskJob(TaskContext context, SamClubDataImportTaskService hedwigService) {
        super(context, hedwigService);
    }

    @Override
    public List<SamClubHostLog> read(BufferedReader reader) throws Exception {
        List<SamClubHostLog> content = new ArrayList<SamClubHostLog>();
        String line;

        int index = 1;
        while ((line = reader.readLine()) != null) {

            try{
                if(index>1000){
                    throw new Exception("单个文件请不要超过1000条记录");
                }

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
//                hostLog.setQualifyOrgCode(s[2]);
//                hostLog.setQualifyOrgName(s[3]);
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
                    throw new Exception("第" + index + "行数据解析出错,原因[birthday不为yyyy-MM-dd格式]");
                }

//                hostLog.setInfoCompleteInd(s[25]);
//                hostLog.setEmailAddr(s[26]);
//                hostLog.setDriverNbr(s[27]);
//                hostLog.setAcceptPhone(s[28]);
//                hostLog.setReceivedMail(s[29]);
//                hostLog.setRecHomeMail(s[30]);
//                hostLog.setHomeMail(s[31]);
//                hostLog.setRecOffiMail(s.length >= 33 ? s[32] : null);
//                hostLog.setOfficeMail(s.length >= 34 ? s[33] : null);

                try {
                    if(s.length>=35){
//                        hostLog.setLastShopDate(super.dateformat.parse(s[34]));
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
            }catch(ArrayIndexOutOfBoundsException e){
                throw new Exception("第" + index + "行数据解析出错,原因[列数不正确," + e.getMessage() + "]");
            }



            index++;
        }
        return content;
    }

}
