package xf.practice.taskUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MtrackNewTaskJob extends ParseTaskJob {

    public MtrackNewTaskJob(TaskContext context, SamClubDataImportTaskService hedwigService) {
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

                try {
//                    hostLog.setLanguageCode(Integer.parseInt(s[0]));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[LanguageCode不符合规则]");
                }
//                hostLog.setMembershipNbr(s[1]);
//                hostLog.setCardHolderNbr(s[2]);
//                hostLog.setCardholderType(s[3]);
//                hostLog.setChTypeShortDesc(s[4]);
//                hostLog.setCardStatCd(s[5]);
//                hostLog.setFullName(s[6]);
                try {
//                    hostLog.setBirthDate(super.dateformat.parse(s[7]));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[birthday不为yyyy-MM-dd格式]");
                }

                try {
//                    hostLog.setExpireDate(super.dateformat.parse(s[8]));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[ExpireDate不为yyyy-MM-dd格式]");
                }

//                hostLog.setMemberCode(s[9]);
                if (StringUtils.isNumeric(s[10])) {
//                    hostLog.setIssuingClubNbrH(Integer.valueOf(s[10]));
                }
//                hostLog.setCurrStatusCode(s[11]);
                try {
//                    hostLog.setStartDate(super.dateformat.parse(s[12]));
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[StartDate不为yyyy-MM-dd格式]");
                }

                try {
                    if (s.length >= 14 && s[13] != null) {
//                        hostLog.setNextRenewDate(super.dateformat.parse(s[13]));
                    }
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[NextRenewDate不为yyyy-MM-dd格式]");
                }

                try {
                    if (s.length >= 15 && s[14] != null) {
//                        hostLog.setLastRenewDate(super.dateformat.parse(s[14]));
                    }
                } catch (Exception e) {
                    throw new Exception("第" + index + "行数据解析出错,原因[LastRenewDate不为yyyy-MM-dd格式]");
                }

                if (s.length >= 16) {
//                    hostLog.setOrgName(s[15]);
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

