package com.roman.component;

import com.roman.entity.DepartmentRevision;
import org.hibernate.envers.RevisionListener;

public class DepartmentRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        ((DepartmentRevision) revisionEntity).setUserName("User");
    }
}
