package org.stepic.droid.concurrency;

import com.squareup.otto.Bus;

import org.stepic.droid.base.MainApplication;
import org.stepic.droid.core.IShell;
import org.stepic.droid.events.courses.FinishingSaveCoursesToDbEvent;
import org.stepic.droid.events.courses.StartingSaveCoursesToDbEvent;
import org.stepic.droid.model.Course;
import org.stepic.droid.store.operations.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ToDbCoursesTask extends StepicTask<Void, Void, Void> {

    @Inject
    IShell mShell;
    @Inject
    Bus bus;
    @Inject
    DatabaseManager mDatabaseManager;

    private List<Course> mCourses;
    private DatabaseManager.Table mCourseType;

    public ToDbCoursesTask(List<Course> courses, DatabaseManager.Table type) {
        super(MainApplication.getAppContext());
        MainApplication.component().inject(this);

        //courses now is not thread safe
        mCourseType = type;
        mCourses = courses;
    }

    public ToDbCoursesTask(Course course, DatabaseManager.Table type) {
        super(MainApplication.getAppContext());
        MainApplication.component().inject(this);

        //courses now is not thread safe
        mCourseType = type;
        mCourses = new ArrayList<>();
        mCourses.add(course);
    }

    @Override
    protected Void doInBackgroundBody(Void... params) throws Exception {

        for (Course courseItem : mCourses) {
            mDatabaseManager.addCourse(courseItem, mCourseType);
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        bus.post(new StartingSaveCoursesToDbEvent(mCourseType));
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(AsyncResultWrapper<Void> voidAsyncResultWrapper) {
        super.onPostExecute(voidAsyncResultWrapper);
        bus.post(new FinishingSaveCoursesToDbEvent(mCourseType));
    }
}
