const AUTH_STORAGE_KEY = 'authHeader';
const USER_STORAGE_KEY = 'currentUser';
const THEME_STORAGE_KEY = 'uiTheme';

function requireAuth() {
    const token = sessionStorage.getItem(AUTH_STORAGE_KEY);
    if (!token) {
        window.location.href = '/login.html';
    }
}

requireAuth();

const originalFetch = window.fetch.bind(window);
window.fetch = (url, options = {}) => {
    const authHeader = sessionStorage.getItem(AUTH_STORAGE_KEY);
    const headers = { ...(options.headers || {}) };
    if (authHeader) {
        headers['Authorization'] = authHeader;
    }
    return originalFetch(url, { ...options, headers });
};

const arraysModal = document.getElementById('arraysModal');
const functionModal = document.getElementById('functionModal');
const settingsModal = document.getElementById('settingsModal');
const operationsModal = document.getElementById('operationsModal');
const derivativeModal = document.getElementById('derivativeModal');
const inspectorModal = document.getElementById('inspectorModal');
const compositeModal = document.getElementById('compositeModal');

const buildTableBtn = document.getElementById('buildTable');
const createFromArraysBtn = document.getElementById('createFromArrays');
const createFromFunctionBtn = document.getElementById('createFromFunction');
const valuesTableBody = document.querySelector('#valuesTable tbody');
const resultsContainer = document.getElementById('results');
const arraysStatus = document.getElementById('arraysStatus');
const functionStatus = document.getElementById('functionStatus');
const importStatus = document.getElementById('importStatus');
const settingsStatus = document.getElementById('settingsStatus');
const operationsStatus = document.getElementById('operationsStatus');
const derivativeStatus = document.getElementById('derivativeStatus');
const inspectStatus = document.getElementById('inspectStatus');
const compositeStatus = document.getElementById('compositeStatus');
const functionSelect = document.getElementById('functionSelect');
const factorySelect = document.getElementById('factorySelect');
const factoryState = document.getElementById('factoryState');
const themeSelect = document.getElementById('themeSelect');
const logoutButton = document.getElementById('logoutButton');
const currentUserLabel = document.getElementById('currentUserLabel');

const operandATable = document.getElementById('operandATable');
const operandBTable = document.getElementById('operandBTable');
const resultTable = document.getElementById('resultTable');
const sourceTable = document.getElementById('sourceTable');
const derivativeTable = document.getElementById('derivativeTable');
const inspectTable = document.getElementById('inspectTable');
const applyInput = document.getElementById('applyX');
const applyResult = document.getElementById('applyResult');
const compositeOuter = document.getElementById('compositeOuter');
const compositeInner = document.getElementById('compositeInner');
const recentASelect = document.getElementById('recentASelect');
const recentBSelect = document.getElementById('recentBSelect');
const recentDerivativeSelect = document.getElementById('recentDerivativeSelect');
const hiddenFileImport = document.getElementById('hiddenFileImport');
const importButton = document.getElementById('importFromFile');
const arrayNameInput = document.getElementById('arrayName');
const functionDisplayNameInput = document.getElementById('functionDisplayName');
const operandANameInput = document.getElementById('operandAName');
const operandBNameInput = document.getElementById('operandBName');
const resultNameInput = document.getElementById('resultName');
const derivativeSourceNameInput = document.getElementById('derivativeSourceName');
const derivativeResultNameInput = document.getElementById('derivativeResultName');
const operandANameDisplay = document.getElementById('operandANameDisplay');
const operandBNameDisplay = document.getElementById('operandBNameDisplay');
const resultNameDisplay = document.getElementById('resultNameDisplay');
const derivativeSourceNameDisplay = document.getElementById('derivativeSourceNameDisplay');
const derivativeResultNameDisplay = document.getElementById('derivativeResultNameDisplay');
const inspectTitle = document.getElementById('inspectTitle');

const MAX_ABS_VALUE = 1e9;

function getThemeStorageKey() {
    try {
        const rawUser = sessionStorage.getItem(USER_STORAGE_KEY);
        if (rawUser) {
            const parsed = JSON.parse(rawUser);
            if (parsed?.login) {
                return `${THEME_STORAGE_KEY}:${parsed.login}`;
            }
        }
    } catch (_) {
        // fallback to default key
    }
    return THEME_STORAGE_KEY;
}

function getSavedTheme() {
    return localStorage.getItem(getThemeStorageKey()) === 'light' ? 'light' : 'dark';
}

function applyTheme(theme) {
    const normalized = theme === 'light' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', normalized);
    localStorage.setItem(getThemeStorageKey(), normalized);
    if (themeSelect) {
        themeSelect.value = normalized;
    }
}

applyTheme(getSavedTheme());

function getCurrentUser() {
    const raw = sessionStorage.getItem(USER_STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
}

function bindUserBar() {
    const user = getCurrentUser();
    if (currentUserLabel && user) {
        currentUserLabel.textContent = `Вы вошли как ${user.login} (роль: ${user.role})`;
    }

    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            sessionStorage.removeItem(AUTH_STORAGE_KEY);
            sessionStorage.removeItem(USER_STORAGE_KEY);
            window.location.href = '/login.html';
        });
    }
}

let activeReceiver = null;
let modalLayer = 20;
const recentFunctions = [];
let recentId = 0;
let operands = { a: null, b: null };
let operationResult = null;
let derivativeSource = null;
let derivativeResult = null;
let inspectedFunction = null;
let inspectChart = null;
let operationsChart = null;
let derivativeChart = null;

function toggleModal(modal, show) {
    if (show) {
        prepareModal(modal);
        modalLayer += 1;
        modal.style.zIndex = modalLayer;
        modal.classList.add('modal--visible');
        modal.setAttribute('aria-hidden', 'false');
    } else {
        modal.classList.remove('modal--visible');
        modal.setAttribute('aria-hidden', 'true');
        resetModalState(modal);
    }
}

function prepareModal(modal) {
    if (modal === arraysModal) {
        showStatus(arraysStatus, '', false);
    } else if (modal === functionModal) {
        showStatus(functionStatus, '', false);
    } else if (modal === settingsModal) {
            showStatus(settingsStatus, '', false);
            loadFactoryState();
            syncThemeControl();
    }
}

function resetModalState(modal) {
    if (modal === operationsModal) {
        resetOperationsModal();
    } else if (modal === derivativeModal) {
        resetDerivativeModal();
    } else if (modal === inspectorModal) {
        resetInspectorModal();
    } else if (modal === arraysModal) {
        resetArraysModal();
    } else if (modal === functionModal) {
        resetFunctionModal();
    } else if (modal === compositeModal) {
        resetCompositeModal();
    } else if (modal === settingsModal) {
         resetSettingsModal();
    }
}

function normalizedName(input, fallback) {
    const value = (input?.value || '').trim();
    return value || fallback || '';
}

function updateNameDisplay(el, name, fallback = 'не задано') {
    if (el) {
        el.textContent = (name || '').trim() || fallback;
    }
}

function applyName(data, input, fallback, display) {
    if (!data) return;
    const chosen = normalizedName(input, data.name || fallback);
    if (input && !input.value.trim()) {
        input.value = chosen;
    }
    data.name = chosen;
    updateNameDisplay(display, data.name);
}

function attachOpeners(buttonIds, modal) {
    buttonIds.forEach(id => {
        const btn = document.getElementById(id);
        if (btn) {
            btn.addEventListener('click', () => toggleModal(modal, true));
        }
    });
}

attachOpeners(['openArrays', 'openArraysSecondary'], arraysModal);
attachOpeners(['openFunction', 'openFunctionSecondary'], functionModal);
attachOpeners(['openSettings'], settingsModal);
attachOpeners(['openOperations', 'openOperationsSecondary'], operationsModal);
attachOpeners(['openDerivative', 'openDerivativeSecondary'], derivativeModal);
attachOpeners(['openInspector', 'openInspectorSecondary'], inspectorModal);
attachOpeners(['openComposite', 'openCompositeSecondary'], compositeModal);

document.querySelectorAll('[data-close]').forEach(btn => {
    const target = document.getElementById(btn.dataset.close);
    btn.addEventListener('click', () => toggleModal(target, false));
});

function renderRows(count) {
    if (valuesTableBody.children.length && !confirm('Пересоздать таблицу и очистить введенные значения?')) {
        return;
    }
    valuesTableBody.innerHTML = '';
    for (let i = 0; i < count; i++) {
        const row = document.createElement('tr');
        const xCell = document.createElement('td');
        const yCell = document.createElement('td');

        const xInput = document.createElement('input');
        xInput.type = 'text';
        xInput.placeholder = `x${i + 1}`;

        const yInput = document.createElement('input');
        yInput.type = 'text';
        yInput.placeholder = `y${i + 1}`;

        xCell.appendChild(xInput);
        yCell.appendChild(yInput);
        row.appendChild(xCell);
        row.appendChild(yCell);
        valuesTableBody.appendChild(row);
    }
}

buildTableBtn.addEventListener('click', () => {
    const count = Number.parseInt(document.getElementById('pointsCount').value, 10);
    if (Number.isNaN(count)) {
        showStatus(arraysStatus, 'Введите количество точек', true);
        return;
    }
    if (count < 2 || count > 300) {
        showStatus(arraysStatus, 'Количество точек должно быть от 2 до 300', true);
        return;
    }
    showStatus(arraysStatus, 'Таблица готова к заполнению', false);
    renderRows(count);
});

function extractTableValues() {
    const rows = Array.from(valuesTableBody.querySelectorAll('tr'));
    const xValues = [];
    const yValues = [];
    const points = [];

    rows.forEach((row, index) => {
        const [xInput, yInput] = row.querySelectorAll('input');
        const parsedX = parseNumber(xInput.value, `x${index + 1}`);
        const parsedY = parseNumber(yInput.value, `y${index + 1}`);
        xValues.push(parsedX.raw.trim());
        yValues.push(parsedY.raw.trim());
        points.push({ x: parsedX.number, y: parsedY.number });
    });

    return { xValues, yValues, points };
}

function parseNumber(value, label) {
    if (!value || value.trim() === '') {
        throw new Error(`Поле ${label} не заполнено`);
    }
    const normalized = value.replace(',', '.');
    const parsed = Number.parseFloat(normalized);
    if (!Number.isFinite(parsed)) {
        throw new Error(`Поле ${label} должно быть числом`);
    }
    if (Math.abs(parsed) > MAX_ABS_VALUE) {
        throw new Error(`Значение ${label} выходит за пределы ±${MAX_ABS_VALUE}`);
    }
    return { number: parsed, raw: normalized };
}

async function sendJson(url, payload) {
    const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: 'Неизвестная ошибка' }));
        throw new Error(error.message || 'Ошибка запроса');
    }
    return response.json();
}

createFromArraysBtn.addEventListener('click', async () => {
    try {
        const count = Number.parseInt(document.getElementById('pointsCount').value, 10);
        if (!Number.isInteger(count)) {
            showStatus(arraysStatus, 'Введите корректное количество точек', true);
            return;
        }
        const { xValues, yValues, points } = extractTableValues();
        if (xValues.length !== count || yValues.length !== count) {
            showStatus(arraysStatus, 'Сначала сформируйте таблицу и заполните все значения', true);
            return;
        }
        const data = await sendJson('/ui/tabulated/from-arrays', {
            pointsCount: count,
            xValues,
            yValues,
            points,
            name: normalizedName(arrayNameInput)
        });
        handleSuccess(data, 'Табулированная функция создана через таблицу');
        toggleModal(arraysModal, false);
    } catch (err) {
        showStatus(arraysStatus, err.message, true);
    }
});

createFromFunctionBtn.addEventListener('click', async () => {
    try {
        const functionName = functionSelect.value;
        const from = document.getElementById('intervalFrom').value;
        const to = document.getElementById('intervalTo').value;
        const pointsCount = Number.parseInt(document.getElementById('functionPoints').value, 10);
        if (!functionName) {
            throw new Error('Выберите функцию из списка');
        }
        if (!Number.isInteger(pointsCount)) {
            throw new Error('Укажите количество точек');
        }
        parseNumber(from, 'Начало интервала');
        parseNumber(to, 'Конец интервала');

        const data = await sendJson('/ui/tabulated/from-function', { functionName, from, to, pointsCount, name: normalizedName(functionDisplayNameInput) });
        handleSuccess(data, 'Табулированная функция создана через формулу');
        toggleModal(functionModal, false);
    } catch (err) {
        showStatus(functionStatus, err.message, true);
    }
});

function normalizeFunctionData(data) {
    if (!data) return data;
    const normalized = { ...data };
    normalized.points = Array.isArray(normalized.points)
        ? data.points
            .map(p => ({ x: Number(p.x), y: Number(p.y) }))
            .filter(p => Number.isFinite(p.x) && Number.isFinite(p.y))
            .sort((a, b) => a.x - b.x)
        : [];

    if (!Number.isFinite(normalized.leftBound) || !Number.isFinite(normalized.rightBound)) {
        if (normalized.points.length) {
            normalized.leftBound = normalized.points[0].x;
            normalized.rightBound = normalized.points[normalized.points.length - 1].x;
        } else {
            normalized.leftBound = undefined;
            normalized.rightBound = undefined;
        }
    }

    return normalized;
}

function showStatus(el, message, isError) {
    if (!el) return;
    el.textContent = message;
    el.classList.toggle('status--error', isError);
    el.classList.toggle('status--success', !isError && message);
}

function handleSuccess(data, message) {
    const normalized = normalizeFunctionData(data);
    showStatus(arraysStatus, '', false);
    showStatus(functionStatus, '', false);
    if (normalized && !normalized.name) {
        normalized.name = message;
    }
    if (activeReceiver) {
        activeReceiver(normalized);
        activeReceiver = null;
    }
    renderResult(normalized, message);
}

function registerRecentFunction(data) {
    const enriched = normalizeFunctionData(data);
    const copy = JSON.parse(JSON.stringify(enriched));
    const entry = { id: `fn-${Date.now()}-${recentId++}`, data: copy };
    recentFunctions.unshift(entry);
    if (recentFunctions.length > 30) {
        recentFunctions.pop();
    }
    refreshRecentSelects();
    return entry.id;
}

function removeRecent(id) {
    const index = recentFunctions.findIndex(item => item.id === id);
    if (index >= 0) {
        recentFunctions.splice(index, 1);
    }
}

function refreshRecentSelects() {
    const selects = [recentASelect, recentBSelect, recentDerivativeSelect];
    selects.forEach(select => {
        if (!select) return;
        const previous = select.value;
        select.innerHTML = '';
        const placeholder = document.createElement('option');
        placeholder.value = '';
        placeholder.textContent = 'Выберите функцию';
        select.appendChild(placeholder);

        recentFunctions.forEach(entry => {
            const option = document.createElement('option');
            option.value = entry.id;
            option.textContent = entry.data.name || 'Без названия';
            select.appendChild(option);
        });

        if (previous && select.querySelector(`option[value="${previous}"]`)) {
            select.value = previous;
        }
    });
}

function useRecentSelection(select, setter, statusEl) {
    try {
        if (!select) {
            throw new Error('Список недоступен');
        }
        const id = select.value;
        if (!id) {
            throw new Error('Выберите функцию из списка');
        }
        const entry = recentFunctions.find(item => item.id === id);
        if (!entry) {
            throw new Error('Функция не найдена');
        }
        const clone = JSON.parse(JSON.stringify(entry.data));
        setter(clone);
        showStatus(statusEl, '', false);
    } catch (err) {
        showStatus(statusEl, err.message, true);
    }
}

function renderResult(data, title) {
    if (!data) return;
    const inspectId = registerRecentFunction(data);
    const card = document.createElement('div');
    card.className = 'result-card';
    const pointsCount = data.points.length;
    const heading = data.name ? `<h4>${data.name}</h4><p class="muted">${title}</p>` : `<h4>${title}</h4>`;
    card.innerHTML = `
        ${heading}
        <div class="result-meta">
            <span class="badge">${pointsCount} точек</span>
            <span class="badge">[${data.leftBound}; ${data.rightBound}]</span>
        </div>
        <div class="table-wrapper">
            <table class="result-table">
                <thead><tr><th>x</th><th>y</th></tr></thead>
                <tbody>
                    ${data.points.map(p => `<tr><td>${p.x}</td><td>${p.y}</td></tr>`).join('')}
                </tbody>
            </table>
        </div>
        <div class="result-actions">
            <button class="btn secondary" data-inspect-id="${inspectId}">Показать график</button>
            <button class="btn ghost" data-remove-id="${inspectId}">Удалить</button>
        </div>
    `;
    resultsContainer.prepend(card);
}

resultsContainer.addEventListener('click', (e) => {
    const deleteBtn = e.target.closest('[data-remove-id]');
    if (deleteBtn) {
        const removeId = deleteBtn.dataset.removeId;
        const card = deleteBtn.closest('.result-card');
        removeRecent(removeId);
        if (card) card.remove();
        refreshRecentSelects();
        return;
    }

    const btn = e.target.closest('[data-inspect-id]');
    if (!btn) return;
    const inspectId = btn.dataset.inspectId;
    const entry = recentFunctions.find(item => item.id === inspectId);
    if (!entry) return;
    inspectedFunction = JSON.parse(JSON.stringify(entry.data));
    renderInspectTable();
    toggleModal(inspectorModal, true);
});

async function loadFunctions() {
    try {
        const response = await fetch('/ui/tabulated/functions');
        const functions = await response.json();
        populateFunctionOptions(functions);
    } catch (err) {
        showStatus(functionStatus, 'Не удалось загрузить функции', true);
    }
}

async function loadFactoryState() {
    try {
        const state = await fetch('/ui/state/factory').then(r => r.json());
        factorySelect.value = state.type;
        factoryState.textContent = state.displayName;
    } catch (err) {
        factoryState.textContent = 'Неизвестно';
    }
}

async function saveFactoryState() {
    try {
        const type = factorySelect.value;
        const selectedTheme = themeSelect ? themeSelect.value : getSavedTheme();
        applyTheme(selectedTheme);
        const state = await sendJson('/ui/state/factory', { type });
        factoryState.textContent = state.displayName;
        showStatus(settingsStatus, 'Настройки сохранены', false);
        toggleModal(settingsModal, false);
    } catch (err) {
        showStatus(settingsStatus, err.message, true);
    }
}

function syncThemeControl() {
    if (themeSelect) {
        themeSelect.value = getSavedTheme();
    }
}

document.getElementById('saveFactory').addEventListener('click', saveFactoryState);

function renderFunctionTable(table, data, editable, onChange) {
    const tbody = table.querySelector('tbody');
    tbody.innerHTML = '';
    if (!data || !data.points || !data.points.length) {
        const row = document.createElement('tr');
        const cell = document.createElement('td');
        cell.colSpan = 2;
        cell.textContent = 'Функция не загружена';
        cell.className = 'muted';
        row.appendChild(cell);
        tbody.appendChild(row);
        return;
    }

    data.points.forEach((point, idx) => {
        const row = document.createElement('tr');
        const xCell = document.createElement('td');
        if (editable) {
            const input = document.createElement('input');
            input.type = 'text';
            input.value = point.x;
            input.addEventListener('change', () => {
                try {
                    const parsed = parseNumber(input.value, `x${idx + 1}`);
                    data.points[idx].x = parsed.number;
                    data.points.sort((a, b) => a.x - b.x);
                    renderFunctionTable(table, data, editable, onChange);
                    if (onChange) onChange();
                } catch (err) {
                    alert(err.message);
                    input.value = point.x;
                }
            });
            xCell.appendChild(input);
        } else {
            xCell.textContent = point.x;
        }
        const yCell = document.createElement('td');
        if (editable) {
            const input = document.createElement('input');
            input.type = 'text';
            input.value = point.y;
            input.addEventListener('change', () => {
                try {
                    const parsed = parseNumber(input.value, `y${idx + 1}`);
                    data.points[idx].y = parsed.number;
                    input.value = parsed.number;
                    if (onChange) onChange();
                } catch (err) {
                    alert(err.message);
                    input.value = point.y;
                }
            });
            yCell.appendChild(input);
        } else {
            yCell.textContent = point.y;
        }
        row.appendChild(xCell);
        row.appendChild(yCell);
        tbody.appendChild(row);
    });
}

function setOperand(key, data) {
    const normalized = normalizeFunctionData(data);
    if (key === 'a') {
        operands.a = normalized;
        applyName(operands.a, operandANameInput, 'Функция A', operandANameDisplay);
        renderFunctionTable(operandATable, operands.a, true, redrawOperationsChart);
    } else {
        operands.b = normalized;
        applyName(operands.b, operandBNameInput, 'Функция B', operandBNameDisplay);
        renderFunctionTable(operandBTable, operands.b, true, redrawOperationsChart);
    }
    redrawOperationsChart();
}

function setDerivativeSource(data) {
    derivativeSource = normalizeFunctionData(data);
    applyName(derivativeSource, derivativeSourceNameInput, 'Исходная функция', derivativeSourceNameDisplay);
    renderFunctionTable(sourceTable, derivativeSource, true, redrawDerivativeChart);
    redrawDerivativeChart();
}

function setDerivativeResult(data) {
    derivativeResult = normalizeFunctionData(data);
    applyName(derivativeResult, derivativeResultNameInput, 'Производная', derivativeResultNameDisplay);
    renderFunctionTable(derivativeTable, derivativeResult, false, redrawDerivativeChart);
    redrawDerivativeChart();
}

function buildDataset(label, color, data) {
    return {
        label,
        data: (data?.points || []).map(p => ({ x: Number(p.x), y: Number(p.y) })),
        borderColor: color,
        backgroundColor: color,
        tension: 0.2,
        fill: false
    };
}

function redrawOperationsChart() {
    const ctx = document.getElementById('operationsChart');
    if (!ctx) return;
    if (operationsChart) operationsChart.destroy();
    const datasets = [];
    if (operands.a?.points?.length) datasets.push(buildDataset('Функция A', '#2563eb', operands.a));
    if (operands.b?.points?.length) datasets.push(buildDataset('Функция B', '#10b981', operands.b));
    if (operationResult?.points?.length) datasets.push(buildDataset('Результат', '#f97316', operationResult));
    if (!datasets.length) return;
    operationsChart = new Chart(ctx, {
        type: 'line',
        data: { datasets },
        options: {
            responsive: true,
            parsing: { xAxisKey: 'x', yAxisKey: 'y' },
            scales: { x: { type: 'linear' } },
            plugins: { legend: { display: true } }
        }
    });
}

function redrawDerivativeChart() {
    const ctx = document.getElementById('derivativeChart');
    if (!ctx) return;
    if (derivativeChart) derivativeChart.destroy();
    const datasets = [];
    if (derivativeSource?.points?.length) datasets.push(buildDataset('Исходная функция', '#2563eb', derivativeSource));
    if (derivativeResult?.points?.length) datasets.push(buildDataset('Производная', '#f59e0b', derivativeResult));
    if (!datasets.length) return;
    derivativeChart = new Chart(ctx, {
        type: 'line',
        data: { datasets },
        options: {
            responsive: true,
            parsing: { xAxisKey: 'x', yAxisKey: 'y' },
            scales: { x: { type: 'linear' } },
            plugins: { legend: { display: true } }
        }
    });
}

function resetSettingsModal() {
    showStatus(settingsStatus, '', false);
    syncThemeControl();
}

function resetArraysModal() {
    const pointsCountInput = document.getElementById('pointsCount');
    if (pointsCountInput) pointsCountInput.value = '';
    valuesTableBody.innerHTML = '';
    if (arrayNameInput) arrayNameInput.value = '';
    showStatus(arraysStatus, '', false);
}

function resetFunctionModal() {
    const fromInput = document.getElementById('intervalFrom');
    const toInput = document.getElementById('intervalTo');
    const pointsInput = document.getElementById('functionPoints');
    if (fromInput) fromInput.value = '';
    if (toInput) toInput.value = '';
    if (pointsInput) pointsInput.value = '';
    if (functionDisplayNameInput) functionDisplayNameInput.value = '';
    if (functionSelect && functionSelect.options.length) {
        functionSelect.selectedIndex = 0;
    }
    showStatus(functionStatus, '', false);
}

function resetCompositeModal() {
    const compositeNameInput = document.getElementById('compositeName');
    if (compositeNameInput) compositeNameInput.value = '';
    if (compositeOuter && compositeOuter.options.length) compositeOuter.selectedIndex = 0;
    if (compositeInner && compositeInner.options.length) compositeInner.selectedIndex = 0;
    showStatus(compositeStatus, '', false);
}

function resetOperationsModal() {
    operands = { a: null, b: null };
    operationResult = null;
    renderFunctionTable(operandATable, operands.a, true, redrawOperationsChart);
    renderFunctionTable(operandBTable, operands.b, true, redrawOperationsChart);
    renderFunctionTable(resultTable, operationResult, false, redrawOperationsChart);
    if (operandANameInput) operandANameInput.value = '';
    if (operandBNameInput) operandBNameInput.value = '';
    if (resultNameInput) resultNameInput.value = '';
    updateNameDisplay(operandANameDisplay, null);
    updateNameDisplay(operandBNameDisplay, null);
    updateNameDisplay(resultNameDisplay, null);
    if (operationsChart) {
        operationsChart.destroy();
        operationsChart = null;
    }
    if (recentASelect) recentASelect.value = '';
    if (recentBSelect) recentBSelect.value = '';
    showStatus(operationsStatus, '', false);
}

function resetDerivativeModal() {
    derivativeSource = null;
    derivativeResult = null;
    renderFunctionTable(sourceTable, derivativeSource, true, redrawDerivativeChart);
    renderFunctionTable(derivativeTable, derivativeResult, false, redrawDerivativeChart);
    if (derivativeSourceNameInput) derivativeSourceNameInput.value = '';
    if (derivativeResultNameInput) derivativeResultNameInput.value = '';
    updateNameDisplay(derivativeSourceNameDisplay, null);
    updateNameDisplay(derivativeResultNameDisplay, null);
    if (derivativeChart) {
        derivativeChart.destroy();
        derivativeChart = null;
    }
    if (recentDerivativeSelect) recentDerivativeSelect.value = '';
    showStatus(derivativeStatus, '', false);
}

function resetInspectorModal() {
    inspectedFunction = null;
    if (inspectChart) {
        inspectChart.destroy();
        inspectChart = null;
    }
    if (applyInput) applyInput.value = '';
    if (applyResult) applyResult.textContent = '';
    if (inspectTitle) inspectTitle.textContent = 'График табулированной функции';
    renderFunctionTable(inspectTable, inspectedFunction, true, () => {});
    showStatus(inspectStatus, '', false);
}

function toPayload(data) {
    return { points: (data?.points || []).map(p => ({ x: Number(p.x), y: Number(p.y) })), name: data?.name };
}

async function saveFunction(data, filename, statusEl) {
    if (!data || !data.points || !data.points.length) {
        showStatus(statusEl, 'Нечего сохранять', true);
        return;
    }
    try {
        const response = await fetch('/ui/tabulated/serialize', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(toPayload(data))
        });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Не удалось сохранить файл');
        }
        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(url);
        showStatus(statusEl, 'Файл сохранён', false);
    } catch (err) {
        showStatus(statusEl, err.message, true);
    }
}

async function loadFunction(fileInput, setter, statusEl) {
    fileInput.value = '';
    fileInput.onchange = async (event) => {
        const file = event.target.files[0];
        if (!file) return;
        const formData = new FormData();
        formData.append('file', file);
        try {
            const lower = file.name.toLowerCase();
            let endpoint = '/ui/tabulated/deserialize';
            if (lower.endsWith('.json')) {
                endpoint = '/ui/tabulated/deserialize/json';
            } else if (lower.endsWith('.xml')) {
                endpoint = '/ui/tabulated/deserialize/xml';
            }
            const response = await fetch(endpoint, { method: 'POST', body: formData });
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Не удалось загрузить функцию');
            }
            const data = await response.json();
            setter(data);
            showStatus(statusEl, '', false);
        } catch (err) {
            showStatus(statusEl, err.message, true);
        }
    };
    fileInput.click();
}

if (importButton) {
    importButton.addEventListener('click', () => loadFunction(hiddenFileImport, (data) => {
        handleSuccess(data, 'Функция загружена из файла');
    }, importStatus));
}

function setupOperandButtons() {
    document.getElementById('createAFromArrays').addEventListener('click', () => {
        activeReceiver = (data) => {
            setOperand('a', data);
        };
        toggleModal(arraysModal, true);
    });
    document.getElementById('createAFromFunction').addEventListener('click', () => {
        activeReceiver = (data) => {
            setOperand('a', data);
        };
        toggleModal(functionModal, true);
    });
    document.getElementById('createBFromArrays').addEventListener('click', () => {
        activeReceiver = (data) => {
            setOperand('b', data);
        };
        toggleModal(arraysModal, true);
    });
    document.getElementById('createBFromFunction').addEventListener('click', () => {
        activeReceiver = (data) => {
            setOperand('b', data);
        };
        toggleModal(functionModal, true);
    });

    document.getElementById('loadA').addEventListener('click', () => loadFunction(document.getElementById('hiddenFileA'), (data) => {
        setOperand('a', data);
    }, operationsStatus));
    document.getElementById('loadB').addEventListener('click', () => loadFunction(document.getElementById('hiddenFileB'), (data) => {
        setOperand('b', data);
    }, operationsStatus));
    document.getElementById('useRecentA').addEventListener('click', () => useRecentSelection(recentASelect, (data) => {
        setOperand('a', data);
    }, operationsStatus));
    document.getElementById('useRecentB').addEventListener('click', () => useRecentSelection(recentBSelect, (data) => {
        setOperand('b', data);
    }, operationsStatus));
    document.getElementById('saveResultJson').addEventListener('click', () => saveStructured(operationResult, '/ui/tabulated/serialize/json', 'operation_result.json', operationsStatus));
    document.getElementById('saveResultXml').addEventListener('click', () => saveStructured(operationResult, '/ui/tabulated/serialize/xml', 'operation_result.xml', operationsStatus));

    operationsModal.querySelectorAll('[data-op]').forEach(btn => btn.addEventListener('click', () => executeOperation(btn.dataset.op)));
}

async function executeOperation(op) {
    try {
        if (!operands.a || !operands.b) {
            throw new Error('Загрузите обе функции-операнды');
        }
        applyName(operands.a, operandANameInput, 'Функция A', operandANameDisplay);
        applyName(operands.b, operandBNameInput, 'Функция B', operandBNameDisplay);
        const desiredName = normalizedName(resultNameInput, 'Результат операции');
        const payload = {
            operation: op,
            first: toPayload(operands.a),
            second: toPayload(operands.b),
            resultName: desiredName
        };
        const data = await sendJson('/ui/operations/binary', payload);
        operationResult = normalizeFunctionData(data);
        applyName(operationResult, resultNameInput, desiredName, resultNameDisplay);
        renderFunctionTable(resultTable, operationResult, false, redrawOperationsChart);
        redrawOperationsChart();
        renderResult(operationResult, operationResult.name || 'Результат операции');
        showStatus(operationsStatus, 'Операция выполнена', false);
    } catch (err) {
        showStatus(operationsStatus, err.message, true);
    }
}

function setupDerivativeButtons() {
    document.getElementById('createSourceFromArrays').addEventListener('click', () => {
        activeReceiver = (data) => {
            setDerivativeSource(data);
        };
        toggleModal(arraysModal, true);
    });
    document.getElementById('createSourceFromFunction').addEventListener('click', () => {
        activeReceiver = (data) => {
            setDerivativeSource(data);
        };
        toggleModal(functionModal, true);
    });

    document.getElementById('loadSource').addEventListener('click', () => loadFunction(document.getElementById('hiddenFileSource'), (data) => {
        setDerivativeSource(data);
    }, derivativeStatus));
    document.getElementById('useRecentSource').addEventListener('click', () => useRecentSelection(recentDerivativeSelect, (data) => {
        setDerivativeSource(data);
    }, derivativeStatus));

    document.getElementById('runDerivative').addEventListener('click', runDerivative);
    document.getElementById('saveDerivativeJson').addEventListener('click', () => saveStructured(derivativeResult, '/ui/tabulated/serialize/json', 'derivative.json', derivativeStatus));
    document.getElementById('saveDerivativeXml').addEventListener('click', () => saveStructured(derivativeResult, '/ui/tabulated/serialize/xml', 'derivative.xml', derivativeStatus));
}

async function runDerivative() {
    try {
        if (!derivativeSource) {
            throw new Error('Загрузите или создайте исходную функцию');
        }
        applyName(derivativeSource, derivativeSourceNameInput, 'Исходная функция', derivativeSourceNameDisplay);
        const desiredName = normalizedName(derivativeResultNameInput, 'Производная');
        const payload = { function: toPayload(derivativeSource), resultName: desiredName };
        const data = await sendJson('/ui/operations/derivative', payload);
        setDerivativeResult(data);
        renderResult(derivativeResult, derivativeResult.name || 'Результат дифференцирования');
        showStatus(derivativeStatus, 'Производная рассчитана', false);
    } catch (err) {
        showStatus(derivativeStatus, err.message, true);
    }
}

function drawInspectChart() {
    if (!inspectTable || !document.getElementById('inspectChart')) return;
    const ctx = document.getElementById('inspectChart');
    if (inspectChart) { inspectChart.destroy(); }
    if (!inspectedFunction || !inspectedFunction.points?.length) { return; }
    inspectChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: inspectedFunction.points.map(p => p.x),
            datasets: [{ label: 'y', data: inspectedFunction.points.map(p => p.y), borderColor: '#4f46e5', tension: 0.2, fill: false }]
        },
        options: { responsive: true, plugins: { legend: { display: false } } }
    });
}

function renderInspectTable() {
    renderFunctionTable(inspectTable, inspectedFunction, true, () => { drawInspectChart(); });
    drawInspectChart();
    if (inspectTitle) {
        inspectTitle.textContent = inspectedFunction?.name ? `График: ${inspectedFunction.name}` : 'График табулированной функции';
    }
}

document.getElementById('inspectSaveJson').addEventListener('click', () => saveStructured(inspectedFunction, '/ui/tabulated/serialize/json', 'inspected.json', inspectStatus));
document.getElementById('inspectSaveXml').addEventListener('click', () => saveStructured(inspectedFunction, '/ui/tabulated/serialize/xml', 'inspected.xml', inspectStatus));

operandANameInput?.addEventListener('input', () => applyName(operands.a, operandANameInput, 'Функция A', operandANameDisplay));
operandBNameInput?.addEventListener('input', () => applyName(operands.b, operandBNameInput, 'Функция B', operandBNameDisplay));
resultNameInput?.addEventListener('input', () => applyName(operationResult, resultNameInput, 'Результат операции', resultNameDisplay));
derivativeSourceNameInput?.addEventListener('input', () => applyName(derivativeSource, derivativeSourceNameInput, 'Исходная функция', derivativeSourceNameDisplay));
derivativeResultNameInput?.addEventListener('input', () => applyName(derivativeResult, derivativeResultNameInput, 'Производная', derivativeResultNameDisplay));

async function saveStructured(data, url, filename, statusEl) {
    if (!data || !data.points?.length) {
        showStatus(statusEl, 'Нечего сохранять', true);
        return;
    }
    try {
        const response = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(toPayload(data)) });
        if (!response.ok) {
            throw new Error('Ошибка сохранения');
        }
        const blob = await response.blob();
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = filename;
        link.click();
        showStatus(statusEl, 'Функция сохранена', false);
    } catch (err) {
        showStatus(statusEl, err.message, true);
    }
}

document.getElementById('applyBtn').addEventListener('click', async () => {
    try {
        if (!inspectedFunction) throw new Error('Нет функции для вычисления');
        const response = await sendJson('/ui/tabulated/apply', { payload: toPayload(inspectedFunction), x: applyInput.value });
        applyResult.textContent = `y = ${response}`;
    } catch (err) {
        applyResult.textContent = err.message;
    }
});

document.getElementById('createComposite').addEventListener('click', async () => {
    try {
        const name = document.getElementById('compositeName').value;
        const outer = compositeOuter.value;
        const inner = compositeInner.value;
        const list = await sendJson('/ui/tabulated/functions/composite', { displayName: name, outerFunction: outer, innerFunction: inner });
        populateFunctionOptions(list);
        showStatus(compositeStatus, 'Сложная функция добавлена', false);
        toggleModal(compositeModal, false);
    } catch (err) {
        showStatus(compositeStatus, err.message, true);
    }
});

async function populateFunctionOptions(options) {
    functionSelect.innerHTML = '';
    compositeOuter.innerHTML = '';
    compositeInner.innerHTML = '';
    options.forEach(opt => {
        const option = document.createElement('option');
        option.value = opt.key || opt.value;
        option.textContent = opt.displayName || opt.label;
        functionSelect.appendChild(option.cloneNode(true));
        compositeOuter.appendChild(option.cloneNode(true));
        compositeInner.appendChild(option.cloneNode(true));
    });
}

bindUserBar();
loadFunctions();
loadFactoryState();
setupOperandButtons();
setupDerivativeButtons();
refreshRecentSelects();