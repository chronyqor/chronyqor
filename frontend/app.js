// UI State
let isLogin = true;
let currentPage = 0;
let totalPages = 0;

// DOM Elements
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const authForm = document.getElementById('auth-form');
const authTitle = document.getElementById('auth-title');
const authSubmit = document.getElementById('auth-submit');
const authToggleBtn = document.getElementById('auth-toggle-btn');
const authToggleText = document.getElementById('auth-toggle-text');
const emailField = document.getElementById('email-field');
const taskGrid = document.getElementById('task-grid');
const taskCount = document.getElementById('task-count');
const navUser = document.getElementById('nav-user');
const usernameDisplay = document.getElementById('username-display');
const logoutBtn = document.getElementById('logout-btn');
const addTaskBtn = document.getElementById('add-task-btn');
const taskModal = document.getElementById('task-modal');
const closeModal = document.getElementById('close-modal');
const taskForm = document.getElementById('task-form');
const prevPage = document.getElementById('prev-page');
const nextPage = document.getElementById('next-page');
const pageInfo = document.getElementById('page-info');

// Initialization
function init() {
    const token = localStorage.getItem('token');
    if (token) {
        showDashboard();
    } else {
        showAuth();
    }
}

// UI Transitions
function showAuth() {
    authSection.classList.remove('hidden');
    dashboardSection.classList.add('hidden');
    navUser.classList.add('hidden');
}

function showDashboard() {
    authSection.classList.add('hidden');
    dashboardSection.classList.remove('hidden');
    navUser.classList.remove('hidden');
    usernameDisplay.textContent = localStorage.getItem('username');
    loadTasks();
}

// Auth Logic
authToggleBtn.addEventListener('click', () => {
    isLogin = !isLogin;
    authTitle.textContent = isLogin ? 'Welcome Back' : 'Create Account';
    authSubmit.textContent = isLogin ? 'Login' : 'Sign Up';
    authToggleText.textContent = isLogin ? "Don't have an account?" : "Already have an account?";
    authToggleBtn.textContent = isLogin ? 'Create Account' : 'Login';
    emailField.classList.toggle('hidden', isLogin);
    document.getElementById('email').required = !isLogin;
});

authForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;

    try {
        if (isLogin) {
            await api.login(username, password);
            showToast('Login successful!');
        } else {
            await api.register(username, email, password);
            showToast('Account created!');
        }
        showDashboard();
    } catch (err) {
        showToast(err.message, true);
    }
});

logoutBtn.addEventListener('click', () => api.logout());

// Task Logic
async function loadTasks(page = 0) {
    try {
        const res = await api.getTasks(page);
        currentPage = res.pageNumber;
        totalPages = res.totalPages;
        renderTasks(res.content);
        updatePagination();
        taskCount.textContent = res.totalElements;
    } catch (err) {
        showToast(err.message, true);
    }
}

function renderTasks(tasks) {
    if (tasks.length === 0) {
        taskGrid.innerHTML = `
            <div class="col-span-full py-24 text-center glass rounded-2xl">
                <i class="fa-solid fa-clipboard-list text-5xl text-slate-700 mb-4"></i>
                <h3 class="text-xl font-semibold">No tasks yet</h3>
                <p class="text-slate-500">Create your first task to get started.</p>
            </div>
        `;
        return;
    }

    taskGrid.innerHTML = tasks.map(task => `
        <div class="glass p-6 rounded-2xl task-card transition-all animate-fadeIn">
            <div class="flex justify-between items-start mb-4">
                <span class="px-3 py-1 rounded-full text-xs font-bold ${getStatusClass(task.status)}">
                    ${task.status.replace('_', ' ')}
                </span>
                <div class="flex gap-2">
                    <button onclick="editTask(${task.id})" class="text-slate-400 hover:text-primary-400 transition"><i class="fa-solid fa-pen"></i></button>
                    <button onclick="deleteTask(${task.id})" class="text-slate-400 hover:text-red-400 transition"><i class="fa-solid fa-trash"></i></button>
                </div>
            </div>
            <h3 class="text-xl font-bold mb-2">${task.title}</h3>
            <p class="text-slate-400 text-sm mb-6 line-clamp-2">${task.description || 'No description'}</p>
            <div class="flex items-center justify-between text-xs text-slate-500 border-t border-slate-800 pt-4">
                <div class="flex items-center gap-1.5">
                    <i class="fa-solid fa-calendar-day"></i>
                    ${task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'No date'}
                </div>
                <div class="flex items-center gap-1.5">
                    <i class="fa-solid fa-clock"></i>
                    ${new Date(task.createdAt).toLocaleDateString()}
                </div>
            </div>
        </div>
    `).join('');
}

function getStatusClass(status) {
    switch (status) {
        case 'TODO': return 'bg-slate-800 text-slate-300';
        case 'IN_PROGRESS': return 'bg-blue-900/30 text-blue-400 border border-blue-800/50';
        case 'DONE': return 'bg-emerald-900/30 text-emerald-400 border border-emerald-800/50';
        default: return 'bg-slate-800 text-slate-300';
    }
}

// Modal Logic
addTaskBtn.addEventListener('click', () => {
    taskForm.reset();
    document.getElementById('task-id').value = '';
    document.getElementById('modal-title').textContent = 'Create New Task';
    taskModal.classList.remove('hidden');
});

closeModal.addEventListener('click', () => taskModal.classList.add('hidden'));

taskForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('task-id').value;
    const taskData = {
        title: document.getElementById('task-title').value,
        description: document.getElementById('task-desc').value,
        status: document.getElementById('task-status').value,
        dueDate: document.getElementById('task-date').value ? new Date(document.getElementById('task-date').value).toISOString() : null
    };

    try {
        if (id) {
            await api.updateTask(id, taskData);
            showToast('Task updated!');
        } else {
            await api.createTask(taskData);
            showToast('Task created!');
        }
        taskModal.classList.add('hidden');
        loadTasks(currentPage);
    } catch (err) {
        showToast(err.message, true);
    }
});

window.editTask = async (id) => {
    try {
        const task = await api.getTask(id);
        document.getElementById('task-id').value = task.id;
        document.getElementById('task-title').value = task.title;
        document.getElementById('task-desc').value = task.description;
        document.getElementById('task-status').value = task.status;
        if (task.dueDate) {
            document.getElementById('task-date').value = task.dueDate.substring(0, 16);
        }
        document.getElementById('modal-title').textContent = 'Edit Task';
        taskModal.classList.remove('hidden');
    } catch (err) {
        showToast(err.message, true);
    }
};

window.deleteTask = async (id) => {
    if (!confirm('Are you sure you want to delete this task?')) return;
    try {
        await api.deleteTask(id);
        showToast('Task deleted!');
        loadTasks(currentPage);
    } catch (err) {
        showToast(err.message, true);
    }
};

// Pagination Logic
function updatePagination() {
    pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages || 1}`;
    prevPage.disabled = currentPage === 0;
    nextPage.disabled = currentPage >= totalPages - 1 || totalPages === 0;
}

prevPage.addEventListener('click', () => {
    if (currentPage > 0) loadTasks(currentPage - 1);
});

nextPage.addEventListener('click', () => {
    if (currentPage < totalPages - 1) loadTasks(currentPage + 1);
});

// Toast Logic
function showToast(message, isError = false) {
    const toast = document.getElementById('toast');
    const toastMsg = document.getElementById('toast-message');
    const toastIcon = document.getElementById('toast-icon');
    
    toastMsg.textContent = message;
    toast.classList.toggle('border-red-500', isError);
    toast.classList.toggle('border-primary-500', !isError);
    toastIcon.className = isError ? 'fa-solid fa-circle-xmark text-red-400' : 'fa-solid fa-circle-check text-primary-400';
    
    toast.classList.remove('translate-y-24');
    setTimeout(() => {
        toast.classList.add('translate-y-24');
    }, 3000);
}

init();
