const API_BASE = '/api/mercados';

document.addEventListener('DOMContentLoaded', () => {
    loadMercados();
});

async function loadMercados() {
    try {
        const response = await fetch(API_BASE);
        const mercados = await response.json();
        renderTable(mercados);
    } catch (error) {
        showMessage('Error al cargar los mercados: ' + error.message, 'error');
    }
}

function renderTable(mercados) {
    const tbody = document.getElementById('mercados-tbody');
    tbody.innerHTML = '';

    mercados.forEach(mercado => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${mercado.id}</td>
            <td>${mercado.fechaVigencia}</td>
            <td>${mercado.esFija ? 'Sí' : 'No'}</td>
            <td>${mercado.valorTasa}</td>
            <td>
                <button class="btn btn-edit" onclick="editMercado(${mercado.id})">Editar</button>
                <button class="btn btn-delete" onclick="deleteMercado(${mercado.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

document.getElementById('mercado-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('mercado-id').value;
    const data = {
        fechaVigencia: document.getElementById('fecha-vigencia').value,
        esFija: document.getElementById('es-fija').value === 'true',
        valorTasa: parseInt(document.getElementById('valor-tasa').value)
    };

    try {
        let response;
        if (id) {
            response = await fetch(`${API_BASE}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
        } else {
            response = await fetch(API_BASE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
        }

        const result = await response.json();

        if (response.ok) {
            showMessage(result.message, 'success');
            resetForm();
            loadMercados();
        } else {
            showMessage(result.error || 'Error inesperado', 'error');
        }
    } catch (error) {
        showMessage('Error de conexión: ' + error.message, 'error');
    }
});

async function editMercado(id) {
    try {
        const response = await fetch(`${API_BASE}/${id}`);
        if (!response.ok) throw new Error('No encontrado');
        const mercado = await response.json();

        document.getElementById('mercado-id').value = mercado.id;
        document.getElementById('fecha-vigencia').value = mercado.fechaVigencia;
        document.getElementById('es-fija').value = mercado.esFija ? 'true' : 'false';
        document.getElementById('valor-tasa').value = mercado.valorTasa;

        document.getElementById('form-title').textContent = 'Editar Mercado Financiero';
        document.getElementById('btn-submit').textContent = 'Actualizar';
        document.getElementById('btn-cancel').style.display = 'inline-block';

        document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        showMessage('Error al cargar mercado: ' + error.message, 'error');
    }
}

async function deleteMercado(id) {
    if (!confirm('¿Estás seguro de que quieres eliminar este mercado financiero?')) return;

    try {
        const response = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        const result = await response.json();

        if (response.ok) {
            showMessage(result.message, 'success');
            loadMercados();
        } else {
            showMessage(result.error || 'Error al eliminar', 'error');
        }
    } catch (error) {
        showMessage('Error de conexión: ' + error.message, 'error');
    }
}

document.getElementById('btn-cancel').addEventListener('click', () => {
    resetForm();
});

function resetForm() {
    document.getElementById('mercado-form').reset();
    document.getElementById('mercado-id').value = '';
    document.getElementById('form-title').textContent = 'Insertar Mercado Financiero';
    document.getElementById('btn-submit').textContent = 'Guardar';
    document.getElementById('btn-cancel').style.display = 'none';
}

function showMessage(text, type) {
    const msgEl = document.getElementById('message');
    msgEl.textContent = text;
    msgEl.className = `message ${type}`;
    msgEl.style.display = 'block';
    setTimeout(() => { msgEl.style.display = 'none'; }, 4000);
}