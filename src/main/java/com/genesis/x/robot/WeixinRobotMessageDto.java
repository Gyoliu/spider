package com.genesis.x.robot;

import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2020/1/15 14:35
 * @Description:
 */
public class WeixinRobotMessageDto {

    /**
     * 回答问题的集群ID name
     */
    private Integer ans_node_id;
    private String ans_node_name;
    /**
     * 答案
     */
    private String answer;
    /**
     * 答案类型 text
     */
    private String answer_type;
    private Integer answer_open;
    private Stat bid_stat;
    private String create_time;
    private String dialog_status;
    /**
     * 谁问的问题
     */
    private String from_user_name;
    /**
     * 谁回答的问题
     */
    private String to_user_name;
    private String list_options;
    private List<Msg> msg;
    private String scene_status;
    private String session_id;
    private String status;
    private String take_options_only;
    /**
     * 问题
     */
    private String title;

    public static class Stat {
        public Stat(){}
        private String curr_time;
        private String latest_time;
        private String latest_valid;
        private String err_msg;
        private Integer up_ret;

        public String getCurr_time() {
            return curr_time;
        }

        public void setCurr_time(String curr_time) {
            this.curr_time = curr_time;
        }

        public String getLatest_time() {
            return latest_time;
        }

        public void setLatest_time(String latest_time) {
            this.latest_time = latest_time;
        }

        public String getLatest_valid() {
            return latest_valid;
        }

        public void setLatest_valid(String latest_valid) {
            this.latest_valid = latest_valid;
        }

        public String getErr_msg() {
            return err_msg;
        }

        public void setErr_msg(String err_msg) {
            this.err_msg = err_msg;
        }

        public Integer getUp_ret() {
            return up_ret;
        }

        public void setUp_ret(Integer up_ret) {
            this.up_ret = up_ret;
        }
    }

    public static class Msg {
        public Msg(){}
        private Integer confidence;
        private Integer ans_node_id;
        private String ans_node_name;
        private String content;
        /**
         * text,music,news
         */
        private String msg_type;
        private String resp_title;
        private String scene_status;
        private String status;
        private String list_options;
        private String take_options_only;

        public Integer getConfidence() {
            return confidence;
        }

        public void setConfidence(Integer confidence) {
            this.confidence = confidence;
        }

        public Integer getAns_node_id() {
            return ans_node_id;
        }

        public void setAns_node_id(Integer ans_node_id) {
            this.ans_node_id = ans_node_id;
        }

        public String getAns_node_name() {
            return ans_node_name;
        }

        public void setAns_node_name(String ans_node_name) {
            this.ans_node_name = ans_node_name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public String getResp_title() {
            return resp_title;
        }

        public void setResp_title(String resp_title) {
            this.resp_title = resp_title;
        }

        public String getScene_status() {
            return scene_status;
        }

        public void setScene_status(String scene_status) {
            this.scene_status = scene_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getList_options() {
            return list_options;
        }

        public void setList_options(String list_options) {
            this.list_options = list_options;
        }

        public String getTake_options_only() {
            return take_options_only;
        }

        public void setTake_options_only(String take_options_only) {
            this.take_options_only = take_options_only;
        }
    }

    public Integer getAns_node_id() {
        return ans_node_id;
    }

    public void setAns_node_id(Integer ans_node_id) {
        this.ans_node_id = ans_node_id;
    }

    public String getAns_node_name() {
        return ans_node_name;
    }

    public void setAns_node_name(String ans_node_name) {
        this.ans_node_name = ans_node_name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_type() {
        return answer_type;
    }

    public void setAnswer_type(String answer_type) {
        this.answer_type = answer_type;
    }

    public Integer getAnswer_open() {
        return answer_open;
    }

    public void setAnswer_open(Integer answer_open) {
        this.answer_open = answer_open;
    }

    public Stat getBid_stat() {
        return bid_stat;
    }

    public void setBid_stat(Stat bid_stat) {
        this.bid_stat = bid_stat;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDialog_status() {
        return dialog_status;
    }

    public void setDialog_status(String dialog_status) {
        this.dialog_status = dialog_status;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public String getList_options() {
        return list_options;
    }

    public void setList_options(String list_options) {
        this.list_options = list_options;
    }

    public List<Msg> getMsg() {
        return msg;
    }

    public void setMsg(List<Msg> msg) {
        this.msg = msg;
    }

    public String getScene_status() {
        return scene_status;
    }

    public void setScene_status(String scene_status) {
        this.scene_status = scene_status;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTake_options_only() {
        return take_options_only;
    }

    public void setTake_options_only(String take_options_only) {
        this.take_options_only = take_options_only;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}