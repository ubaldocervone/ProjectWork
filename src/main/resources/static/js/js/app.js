
let charts = [];

function makeLine(id, labels, data, yTitle) {
    const ctx = document.getElementById(id).getContext('2d');
    const ch = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: yTitle,
                data: data,
                tension: 0.25,
                fill: false,
                pointRadius: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true },
                tooltip: { mode: 'index', intersect: false }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
    charts.push(ch);
    return ch;
}

function makeBar(id, labels, data, yTitle) {
    const ctx = document.getElementById(id).getContext('2d');
    const ch = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: yTitle,
                data: data,
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true } },
            plugins: { legend: { display: true } }
        }
    });
    charts.push(ch);
    return ch;
}

function downloadPng(canvasId) {
    const link = document.createElement('a');
    link.download = canvasId + '.png';
    link.href = document.getElementById(canvasId).toDataURL('image/png', 1.0);
    link.click();
}

function resetAllCharts() {
    charts.forEach(c => c.reset());
}

// build charts se ci sono dati
window.addEventListener('DOMContentLoaded', () => {
    try {
        if (Array.isArray(LABELS)) {
            makeLine('tempChart', LABELS, TEMP_DATA, 'Temperatura');
            makeLine('humChart', LABELS, HUM_DATA, 'Umidità');
            makeBar('rainChart', LABELS, RAIN_DATA, 'Pioggia');
        }
        if (Array.isArray(P_LABELS)) {
            makeLine('priceChart', P_LABELS, P_DATA, 'Indice');
        }
    } catch(e) {
        console.error(e);
    }
});
