const AUTH_STORAGE_KEY = 'authHeader';
const USER_STORAGE_KEY = 'currentUser';

function storeAuth(login, password, user) {
    sessionStorage.setItem(AUTH_STORAGE_KEY, `Basic ${btoa(`${login}:${password}`)}`);
    sessionStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
}

async function handleLogin(event) {
    event.preventDefault();
    const login = document.getElementById('login').value.trim();
    const password = document.getElementById('password').value;
    const error = document.getElementById('login-error');
    error.textContent = '';

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, password })
        });

        if (!response.ok) {
            error.textContent = 'Неверный логин или пароль';
            return;
        }

        const user = await response.json();
        storeAuth(login, password, user);
        window.location.href = '/app.html';
    } catch (e) {
        error.textContent = 'Не удалось выполнить вход в аккаунт';
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const login = document.getElementById('register-login').value.trim();
    const password = document.getElementById('register-password').value;
    const confirm = document.getElementById('register-confirm').value;
    const error = document.getElementById('register-error');
    error.textContent = '';

    if (password !== confirm) {
        error.textContent = 'Пароли не совпадают';
        return;
    }

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, password, confirmPassword: confirm })
        });

        if (!response.ok) {
            const message = await response.text();
            error.textContent = message || 'Не удалось зарегистрировать аккаунт';
            return;
        }

        window.location.href = '/login.html';
    } catch (e) {
        error.textContent = 'Не удалось зарегистрировать аккаунт';
    }
}

function initAuthForms() {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
}

document.addEventListener('DOMContentLoaded', initAuthForms);