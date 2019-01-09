package com.myreevuuCoach.models;

import java.util.List;

/**
 * Created by dev on 28/11/18.
 */

public class PaymentModel extends ErrorModelJava {


    /**
     * response : {"bank_accounts":[{"id":26,"account_number":"6789","first_name":"Bcncn","last_name":"Ndndn"}],"earning":"610.11"}
     * code : 111
     */

    private ResponseBean response;
    private int code;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResponseBean {
        /**
         * bank_accounts : [{"id":26,"account_number":"6789","first_name":"Bcncn","last_name":"Ndndn"}]
         * earning : 610.11
         */

        private String earning;
        private List<BankAccountsBean> bank_accounts;

        public String getEarning() {
            return earning;
        }

        public void setEarning(String earning) {
            this.earning = earning;
        }

        public List<BankAccountsBean> getBank_accounts() {
            return bank_accounts;
        }

        public void setBank_accounts(List<BankAccountsBean> bank_accounts) {
            this.bank_accounts = bank_accounts;
        }

        public static class BankAccountsBean {
            /**
             * id : 26
             * account_number : 6789
             * first_name : Bcncn
             * last_name : Ndndn
             */

            private int id;
            private String account_number;
            private String first_name;
            private String last_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAccount_number() {
                return account_number;
            }

            public void setAccount_number(String account_number) {
                this.account_number = account_number;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }
        }
    }
}
