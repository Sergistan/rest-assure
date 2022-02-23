import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Resp {

        private ResponseData data;
        private ResponseSupport support;

        public Resp(ResponseData data, ResponseSupport support) {
                this.data = data;
                this.support = support;
        }

        public ResponseData getData() {
                return data;
        }

        public void setData(ResponseData data) {
                this.data = data;
        }

        public ResponseSupport getSupport() {
                return support;
        }

        public void setSupport(ResponseSupport support) {
                this.support = support;
        }
}







