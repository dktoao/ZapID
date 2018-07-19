'use strict'

// Starter table that will be the starting point of every new layout and will
// also demonstrate how the table works
const starter_table = {
    attributes: [
        {
            name: "Field 1",
            value: "Attribute 1",
            print: "full",
        },
        {
            name: "Field 2",
            value: "Attribute 2",
            print: "value",
        },
    ],
    fields: [
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
        ["Value 1", "Value 2"],
        ["Value 3", "Value 4"],
    ],
    options: {
        charwidth: 20,
        charscale: 1,
    },
};

// Global state variable that contains the state of the page
let active_table;
let active_select;
let focus_table;
let focus_col;
let focus_row;

// Initialize a new page
startNew();

function clear() {
    // Clear all elements from the table
    let table = document.getElementById("attribute-table");
    table.deleteTHead();
    let body = table.getElementsByTagName("tbody")[0];
    if (body) {
        table.removeChild(body);
    }
    table = document.getElementById("id-table");
    table.deleteTHead();
    body = table.getElementsByTagName("tbody")[0];
    if (body) {
        table.removeChild(body);
    }
    resetTabCounter();
}

function setFocus(table, col, row) {
    focus_table = table;
    focus_col = col;
    focus_row = row;
}

function resetFocus() {
    if (focus_table == null || focus_col == null || focus_row == null ) {
        return;
    }
    // Get the correct table
    let table;
    let col_shift;
    if (focus_table == "Attribute") {
        table = document.getElementById("attribute-table");
        col_shift = 0;
    } else if (focus_table == "Id") {
        table = document.getElementById("id-table");
        col_shift = 2;
    } else {
        return;
    }
    // Get the correct Table row element
    let row;
    if (focus_row == 0) {
        let body = table.children[0];
        row = body.children[0];
    } else {
        let body = table.children[1];
        row = body.children[focus_row-1];
    }
    // Get the correct input and set focus
    let cell = row.children[focus_col + col_shift];
    let input;
    if (focus_row == 0) {
        let div = cell.children[0];
        input = div.children[0];
    } else {
        input = cell.children[0];
    }
    input.focus();
}

function render() {
    // Delete the tables and then re-create them from the active table.
    clear();
    // Render the attribute table head
    let attribute_table = document.getElementById("attribute-table");
    let head = attribute_table.createTHead();
    let hrow = head.insertRow(0);
    let n_attribute = active_table.attributes.length;
    for (let ii=0; ii<n_attribute; ii++) {
        let cell = createHeader(
            "Attribute", 
            active_table.attributes[ii].name, 
            ii,
            active_table.attributes[ii].print
        );
        hrow.appendChild(cell);
    }
    let add_cell = document.createElement("th");
    let add_link = document.createElement("a");
    add_link.href = "javascript:addAttributeCol();"
    add_link.innerHTML = "+";
    add_cell.appendChild(add_link);
    hrow.appendChild(add_cell);
    // Render all attribute table data
    let body = attribute_table.createTBody();
    let brow = body.insertRow(0);
    for (let ii=0; ii<n_attribute; ii++) {
        let cell = document.createElement("td");
        cell.appendChild(createLinkedCell(
            "Attribute", "Data", 
            active_table.attributes[ii].value, ii));
        brow.appendChild(cell);
    }
    // Then do the ID table
    let id_table = document.getElementById("id-table");
    head = id_table.createTHead();
    hrow = head.insertRow(0);
    let n_fields = active_table.fields.length;
    let checkbox_cell = document.createElement("th");
    let checkbox = document.createElement("input");
    checkbox.type="checkbox";
    checkbox.addEventListener('change', function(e) {
        toggleAllRowSelection(e.target);
    });
    checkbox.checked = active_select.every(
        function(val) {val == true});
    checkbox_cell.appendChild(checkbox);
    hrow.appendChild(checkbox_cell);
    let del_cell = document.createElement("th");
    let del_link = document.createElement("a");
    del_link.href="#";
    del_link.innerHTML="del";
    del_cell.appendChild(del_link);
    hrow.appendChild(del_cell);
    for (let ii=0; ii<n_fields; ii++) {
        let cell = createHeader(
            "Id", 
            active_table.fields[ii].name, 
            ii,
            active_table.fields[ii].print
        );
        hrow.appendChild(cell);
    }
    add_cell = document.createElement("th");
    add_link = document.createElement("a");
    add_link.href = "javascript:addIdCol();"
    add_link.innerHTML = "+";
    add_cell.appendChild(add_link);
    hrow.appendChild(add_cell);
    // Render all ID Table data
    body = id_table.createTBody();
    let n_ids = active_table.id_table.length;
    for (let id_idx=0; id_idx<n_ids; id_idx++) {
        let idrow = body.insertRow(id_idx);
        checkbox_cell = document.createElement("td");
        checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        if (active_select[id_idx] == true) {
            checkbox.checked = true;
        } else {
            checkbox.checked = false;
        }
        checkbox.addEventListener('change', function(e) {
            toggleRowSelection(e.target, id_idx);
        });
        checkbox_cell.appendChild(checkbox);
        idrow.appendChild(checkbox_cell);
        del_cell = document.createElement("td");
        del_link = document.createElement("a");
        del_link.href = "javascript:delIdRow(" + id_idx + ");";
        del_link.innerHTML = "del";
        del_cell.appendChild(del_link);
        idrow.appendChild(del_cell);
        for (let cell_idx=0; cell_idx<n_fields; cell_idx++) {
            let cell = document.createElement("td");
            cell.appendChild(createLinkedCell(
                "Id", "Data", 
                active_table.id_table[id_idx][cell_idx], 
                cell_idx, id_idx
            ));
            idrow.appendChild(cell)
        }
    }
    let add_row = body.insertRow();
    add_cell = document.createElement("td");
    add_row.appendChild(add_cell);
    add_link = document.createElement("a");
    add_link.href = "javascript:addIdRow();"
    add_link.innerHTML = "+";
    add_cell.appendChild(add_link);
    // Reset focus to get the cursor back in the same place
    resetFocus();
}

function deleteAttributeField(index) {
    active_table.attributes.splice(index, 1);
    render();
}

function deleteIdField(index) {
    active_table.fields.splice(index, 1);
    for (let ii=0; ii<active_table.id_table.length; ii++) {
        active_table.id_table[ii].splice(index, 1);
    }
    render();
}

function updateAttributeVisibility(index, value) {
    active_table.attributes[index].print = value;
}

function updateIdVisibility(index, value) {
    active_table.fields[index].print = value;
}

function addAttributeCol() {
    active_table.attributes.push({
        name: "Field",
        value: "Attribute",
        print: "full",
    })
    render();
}

function addIdCol() {
    active_table.fields.push({
        name: "Field",
        print: "full",
    })
    for (let ii=0; ii<active_table.id_table.length; ii++) {
        active_table.id_table[ii].push("");
    }
    render();
}

function addIdRow() {
    let row = new Array(active_table.fields.length).fill("");
    active_table.id_table.push(row);
    active_select.push(false);
    render();
}

function delIdRow(index) {
    active_table.id_table.splice(index, 1);
    active_select.splice(index, 1);
    render();
}

function toggleRowSelection(elem, index) {
    if (elem.checked == true) {
        active_select[index] = true;
    } else {
        active_select[index] = false;
    }
    render();
}

function toggleAllRowSelection(elem) {
    if (elem.checked == true) {
        active_select.fill(true);
    } else {
        active_select.fill(false);
    }
    render();
}

function updateAttributeData(elem, index) {
    active_table.attributes[index].value = elem.value;
    render();
}

function startNew() {
    active_table = JSON.parse(JSON.stringify(starter_table));
    //active_table = Object.assign({}, starter_table);
    active_select = new Array(active_table.id_table.length).fill(false);
    focus_table = null;
    focus_col = null;
    focus_row = null;
    render();
}