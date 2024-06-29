package publisher.impl;

import publisher.api.Event;

public class AbstractEventListener {
    protected Event event;

    public boolean updateInvoked() {
        return this.event != null;
    }

    public <E extends Event> boolean updateInvokedWithEvent(E event) {
        if (this.event == null) {
            return false;
        }
        return event.equals(this.event);
    }
}
