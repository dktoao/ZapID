'use strict'

// Starter table that will be used to demonstrate how the page works
const starter_table = {
    attributes: [
        {
            name: "Attribute Field 1",
            value: "Attribute 1",
            print: "full",
        },
        {
            name: "Attribute Field 2",
            value: "Attribute 2",
            print: "value",
        },
    ],
    id_fields: [
        {
            name: "Field 1",
            print: "full",
        },
        {
            name: "Field 2",
            print: "value",
        },
    ],
    id_table: [
        ["Value 1", "Value 2",],
        ["Value 3", "Value 4",],
    ],
    options: {
        charwidth: 20,
        charscale: 1,
    },
};

let active_table = starter_table;

function clearAll() {
    // Clear all elements from the table
    let table = document.getElementById("attribute-table");
    table.deleteTHead();
    table.removeChild(table.getElementsByTagName("tbody")[0]);
    table = document.getElementById("id-table");
    table.deleteTHead();
    table.removeChild(table.getElementsByTagName("tbody")[0]);
}

function renderAll() {
    // Delete the tables and then re-create them from the active table.
    clearAll();
    // Start with the attribute table
    let attribute_table = document.getElementById("attribute-table");
    let head = attribute_table.createTHead();
    let hrow = head.insertRow(0);
    let n_attribute = active_table.attributes.length;
    for (let ii=0; ii<n_attribute; ii++) {
        let cell = document.createElement("th");
        cell.innerHTML = active_table.attributes[ii].name;
        hrow.appendChild(cell);
    }
    let body = attribute_table.createTBody();
    let brow = body.insertRow(0);
    for (let ii=0; ii<n_attribute; ii++) {
        let cell = document.createElement("td");
        cell.innerHTML = active_table.attributes[ii].value;
        brow.appendChild(cell);
    }
    // Then do the ID table
    let id_table = document.getElementById("id-table");
    head = id_table.createTHead();
    hrow = head.insertRow(0);
    let n_fields = active_table.id_fields.length;
    for (let ii=0; ii<n_attribute; ii++) {
        let cell = document.createElement("th");
        cell.innerHTML = active_table.id_fields[ii].name;
        hrow.appendChild(cell);
    }
    body = id_table.createTBody();
    let n_ids = active_table.id_table.length;
    for (let id_idx=0; id_idx<n_ids; id_idx++) {
        let idrow = body.insertRow(id_idx);
        for (let cell_idx=0; cell_idx<n_fields; cell_idx++) {
            let cell = document.createElement("td");
            cell.innerHTML = active_table.id_table[id_idx][cell_idx];
            idrow.appendChild(cell)
        }
    }
}

function makeNew() {
    clearAll();
}

