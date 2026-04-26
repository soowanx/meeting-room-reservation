document.addEventListener("DOMContentLoaded", () => {
    const config = window.roomReservationConfig || {};
    const dateInput = document.querySelector("[data-reservation-date]");
    const startInput = document.querySelector("[data-start-at]");
    const endInput = document.querySelector("[data-end-at]");
    const startSlotList = document.querySelector("[data-start-slot-list]");
    const endSlotList = document.querySelector("[data-end-slot-list]");
    const slotSummary = document.querySelector("[data-slot-summary]");

    if (!dateInput || !startInput || !endInput || !startSlotList || !endSlotList || !slotSummary) {
        return;
    }

    const existingReservations = (config.existingReservations || []).map((reservation) => ({
        startAt: new Date(reservation.startAt),
        endAt: new Date(reservation.endAt)
    }));

    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    };

    const formatDateTime = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    const formatTimeLabel = (slotMinutes) => {
        const hours = String(Math.floor(slotMinutes / 60)).padStart(2, "0");
        const minutes = String(slotMinutes % 60).padStart(2, "0");
        return `${hours}:${minutes}`;
    };

    const parseLocalDateTime = (value) => {
        if (!value) {
            return null;
        }
        const date = new Date(value);
        return Number.isNaN(date.getTime()) ? null : date;
    };

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const initialStart = parseLocalDateTime(config.initialStartAt);
    const initialEnd = parseLocalDateTime(config.initialEndAt);

    const state = {
        date: initialStart ? formatDate(initialStart) : formatDate(today),
        startMinutes: initialStart ? (initialStart.getHours() * 60) + initialStart.getMinutes() : null,
        endMinutes: initialEnd ? (initialEnd.getHours() * 60) + initialEnd.getMinutes() : null
    };

    dateInput.value = state.date;

    const overlaps = (dateString, startMinutes, endMinutes) => {
        const candidateStart = parseLocalDateTime(`${dateString}T00:00`);
        const candidateEnd = parseLocalDateTime(`${dateString}T00:00`);

        candidateStart.setHours(Math.floor(startMinutes / 60), startMinutes % 60, 0, 0);
        candidateEnd.setHours(Math.floor(endMinutes / 60), endMinutes % 60, 0, 0);

        return existingReservations.some((reservation) => (
            formatDate(reservation.startAt) === dateString
            && candidateStart < reservation.endAt
            && candidateEnd > reservation.startAt
        ));
    };

    const hasAvailableEndSlot = (startMinutes) => {
        for (let endMinutes = startMinutes + 30; endMinutes <= 23 * 60; endMinutes += 30) {
            if (!overlaps(state.date, startMinutes, endMinutes)) {
                return true;
            }
        }
        return false;
    };

    const renderButtons = (container, options, selectedValue, onClick) => {
        container.innerHTML = "";
        options.forEach((option) => {
            const button = document.createElement("button");
            button.type = "button";
            button.className = "slot-chip";
            button.textContent = formatTimeLabel(option.value);
            button.disabled = option.disabled;

            if (selectedValue === option.value) {
                button.classList.add("selected");
            }

            button.addEventListener("click", () => onClick(option.value));
            container.appendChild(button);
        });
    };

    const syncHiddenFields = () => {
        if (state.startMinutes == null) {
            startInput.value = "";
            endInput.value = "";
            slotSummary.textContent = "날짜와 시간을 선택해주세요.";
            return;
        }

        const startDate = parseLocalDateTime(`${state.date}T00:00`);
        startDate.setHours(Math.floor(state.startMinutes / 60), state.startMinutes % 60, 0, 0);
        startInput.value = formatDateTime(startDate);

        if (state.endMinutes == null) {
            endInput.value = "";
            slotSummary.textContent = `${state.date} ${formatTimeLabel(state.startMinutes)} 시작`;
            return;
        }

        const endDate = parseLocalDateTime(`${state.date}T00:00`);
        endDate.setHours(Math.floor(state.endMinutes / 60), state.endMinutes % 60, 0, 0);
        endInput.value = formatDateTime(endDate);
        slotSummary.textContent = `${state.date} ${formatTimeLabel(state.startMinutes)} - ${formatTimeLabel(state.endMinutes)}`;
    };

    const renderEndSlots = () => {
        if (state.startMinutes == null) {
            endSlotList.innerHTML = "";
            syncHiddenFields();
            return;
        }

        const options = [];
        for (let minutes = state.startMinutes + 30; minutes <= 23 * 60; minutes += 30) {
            options.push({
                value: minutes,
                disabled: overlaps(state.date, state.startMinutes, minutes)
            });
        }

        renderButtons(endSlotList, options, state.endMinutes, (value) => {
            if (overlaps(state.date, state.startMinutes, value)) {
                return;
            }
            state.endMinutes = value;
            renderEndSlots();
            syncHiddenFields();
        });
        syncHiddenFields();
    };

    const renderStartSlots = () => {
        const options = [];
        for (let minutes = 9 * 60; minutes <= 22 * 60; minutes += 30) {
            options.push({
                value: minutes,
                disabled: !hasAvailableEndSlot(minutes)
            });
        }

        renderButtons(startSlotList, options, state.startMinutes, (value) => {
            state.startMinutes = value;
            if (state.endMinutes != null && state.endMinutes <= value) {
                state.endMinutes = null;
            }
            renderStartSlots();
            renderEndSlots();
        });
    };

    dateInput.addEventListener("change", () => {
        state.date = dateInput.value;
        state.startMinutes = null;
        state.endMinutes = null;
        renderStartSlots();
        renderEndSlots();
    });

    renderStartSlots();
    renderEndSlots();
});
