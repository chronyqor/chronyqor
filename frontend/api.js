const BASE_URL = '/api/v1';

const api = {
    async request(endpoint, options = {}) {
        const token = localStorage.getItem('token');
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers,
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(`${BASE_URL}${endpoint}`, {
            ...options,
            headers,
        });

        if (response.status === 403 || response.status === 401) {
            // Attempt refresh token
            const refreshToken = localStorage.getItem('refreshToken');
            if (refreshToken && endpoint !== '/auth/refresh-token') {
                try {
                    const refreshRes = await this.refreshToken(refreshToken);
                    localStorage.setItem('token', refreshRes.token);
                    return this.request(endpoint, options);
                } catch (e) {
                    this.logout();
                }
            } else {
                this.logout();
            }
        }

        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.detail || error.message || 'Something went wrong');
        }

        if (response.status === 204) return null;
        return response.json();
    },

    async login(username, password) {
        const res = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        });
        this.setAuth(res);
        return res;
    },

    async register(username, email, password) {
        const res = await this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username, email, password }),
        });
        this.setAuth(res);
        return res;
    },

    async refreshToken(refreshToken) {
        return this.request('/auth/refresh-token', {
            method: 'POST',
            body: JSON.stringify({ refreshToken }),
        });
    },

    setAuth(auth) {
        localStorage.setItem('token', auth.token);
        localStorage.setItem('refreshToken', auth.refreshToken);
        localStorage.setItem('username', auth.username);
    },

    logout() {
        localStorage.clear();
        window.location.reload();
    },

    async getTasks(page = 0, size = 10) {
        return this.request(`/tasks?page=${page}&size=${size}&sort=createdAt,desc`);
    },

    async getTask(id) {
        return this.request(`/tasks/${id}`);
    },

    async createTask(task) {
        return this.request('/tasks', {
            method: 'POST',
            body: JSON.stringify(task),
        });
    },

    async updateTask(id, task) {
        return this.request(`/tasks/${id}`, {
            method: 'PUT',
            body: JSON.stringify(task),
        });
    },

    async deleteTask(id) {
        return this.request(`/tasks/${id}`, {
            method: 'DELETE',
        });
    }
};
