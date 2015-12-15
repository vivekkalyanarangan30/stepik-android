package org.stepic.droid.events.steps;

import org.jetbrains.annotations.Nullable;

public class ClearAllDownloadWithoutAnimationEvent {
    long[] stepIds;

    public ClearAllDownloadWithoutAnimationEvent(long[] stepIds) {
        this.stepIds = stepIds;
    }

    @Nullable
    public long[] getStepIds() {
        return stepIds;
    }
}
