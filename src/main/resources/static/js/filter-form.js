document.addEventListener("DOMContentLoaded", () => {
    const minInput = document.querySelector("[data-min-capacity]");
    const maxInput = document.querySelector("[data-max-capacity]");
    const minLabel = document.querySelector("[data-min-capacity-label]");
    const maxLabel = document.querySelector("[data-max-capacity-label]");
    const activeRange = document.querySelector("[data-range-active]");

    if (!minInput || !maxInput || !minLabel || !maxLabel || !activeRange) {
        return;
    }

    const syncLabels = () => {
        let min = Number(minInput.value);
        let max = Number(maxInput.value);
        const lowerBound = Number(minInput.min);
        const upperBound = Number(minInput.max);

        if (min > max) {
            if (document.activeElement === minInput) {
                max = min;
                maxInput.value = String(max);
            } else {
                min = max;
                minInput.value = String(min);
            }
        }

        minLabel.textContent = String(min);
        maxLabel.textContent = String(max);

        const trackPadding = 16;
        const trackWidth = minInput.offsetWidth - (trackPadding * 2);
        const minPercent = (min - lowerBound) / (upperBound - lowerBound);
        const maxPercent = (max - lowerBound) / (upperBound - lowerBound);
        const left = trackPadding + (trackWidth * minPercent);
        const width = Math.max(0, trackWidth * (maxPercent - minPercent));

        activeRange.style.left = `${left}px`;
        activeRange.style.width = `${width}px`;
    };

    minInput.addEventListener("input", syncLabels);
    maxInput.addEventListener("input", syncLabels);
    syncLabels();
});
