package com.itwillbs.c4d2412t3p1.domain;

public interface WorkableEmployeeProjection {
    String getEmployee_id();
    String getEmployee_cd();
    String getEmployee_nm();

    Integer getTotal_hours();
    Integer getTotal_hours_used();
    Integer getNew_process_time();
    Integer getRemaining_hours();
    Integer getFinal_remaining_hours();
}
