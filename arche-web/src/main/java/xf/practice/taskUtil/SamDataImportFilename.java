package xf.practice.taskUtil;

// 文件名规则
public enum SamDataImportFilename {

        /*全量 */
    NEW {
        @Override
        public String toString() {
            return "cn_mtrack_new.txt";
        }
    },

    /*个人*/
    ADV {
        @Override
        public String toString() {
            return "cn_mtrackadv_allclub.txt";
        }
    },
    /*商业*/
    BM {
        @Override
        public String toString() {
            return "cn_mtrackbm_allclub.txt";
        }
    }
}
