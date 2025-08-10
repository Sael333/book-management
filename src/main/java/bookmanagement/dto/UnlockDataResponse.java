package bookmanagement.dto;

import lombok.Data;

@Data
public class UnlockDataResponse {

        private long date;
        private String lockAlias;
        private String keyStatus;
        private long endDate;
        private long keyId;
        private String lockMac;
        private String deletePwd;
        private String featureValue;
        private int hasGateway;
        private String wirelessKeypadFeatureValue;
        private String lockName;
        private int keyRight;
        private long specialValue;
        private String keyName;
        private String noKeyPwd;
        private int passageMode;
        private long timezoneRawOffset;
        private long lockId;
        private int electricQuantity;
        private String lockData;
        private int keyboardPwdVersion;
        private int remoteEnable;
        private LockVersion lockVersion;
        private String userType;
        private long startDate;
        private String remarks;

        @Data
        public static class LockVersion {
            private boolean showAdminKbpwdFlag;
            private int groupId;
            private int protocolVersion;
            private int protocolType;
            private int orgId;
            private String logoUrl;
            private int scene;
        }
}
