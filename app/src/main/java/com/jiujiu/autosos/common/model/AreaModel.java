package com.jiujiu.autosos.common.model;

import java.util.List;

/**
 * Created by Cary on 2017/4/20 0020.
 */
public class AreaModel {

    /**
     * id : 110000
     * name : 北京市
     * cityList : [{"id":"110100","name":"北京市区","districtList":[{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"}]},{"id":"110200","name":"北京县区","districtList":[{"id":"110228","name":"密云县"},{"id":"110229","name":"延庆县"}]}]
     */

    private String id;
    private String name;
    /**
     * id : 110100
     * name : 北京市区
     * districtList : [{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"}]
     */

    private List<CityListBean> cityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        private String id;
        private String name;
        /**
         * id : 110101
         * name : 东城区
         */

        private List<DistrictListBean> districtList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DistrictListBean> getDistrictList() {
            return districtList;
        }

        public void setDistrictList(List<DistrictListBean> districtList) {
            this.districtList = districtList;
        }

        public static class DistrictListBean {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
