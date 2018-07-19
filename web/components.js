'use strict'

// Components that are used by the main page

function createHeader(loc, name, index, visibility) {
    let value_div = document.createElement("div");
    if (loc == "Attribute") {
        value_div.appendChild(createLinkedCell(
            "Attribute", "Head", name, index));
    } else if (loc == "Id") {
        value_div.appendChild(createLinkedCell(
            "Id", "Head", name, index));
    }
    let visibility_select = createSelect({
        full: "Field: Value",
        value: "Value Only",
        none: "Not Visible",
    })
    visibility_select.value = visibility;
    if (loc == "Attribute") {
        visibility_select.addEventListener('change', (e) => {
            updateAttributeVisibility(index, e.target.value);
        })
    } else if (loc == "Id") {
        visibility_select.addEventListener('change', (e) => {
            updateIdVisibility(index, e.target.value);
        })
    }
    let delete_link = document.createElement("a");
    delete_link.href = "javascript:delete" + loc + "Field(" + index + ");"
    delete_link.innerHTML = "del";
    let field = document.createElement("th");
    field.appendChild(value_div);
    field.appendChild(visibility_select);
    field.appendChild(delete_link);
    return field;
}

function createLinkedCell(loc, type, value, col, row) {
    let input = document.createElement("input");
    input.type = "text";
    input.value = value;
    input.tabIndex = getTabCounter();
    if (loc == "Attribute") {
        if (type == "Head") {
            input.addEventListener('change', (e) => {
                active_table.attributes[col].name = e.target.value;
                render();
            });
            input.addEventListener('focus', (e) => {
                setFocus("Attribute", col, 0);
            });
        } else if (type == "Data") {
            input.addEventListener('change', (e) => {
                active_table.attributes[col].value = e.target.value;
                render();
            });
            input.addEventListener('focus', (e) => {
                setFocus("Attribute", col, 1);
            });
        }
    } else if (loc == "Id") {
        if (type == "Head") {
            input.addEventListener('change', (e) => {
                active_table.fields[col].name = e.target.value;
                render();
            });
            input.addEventListener('focus', (e) => {
                setFocus("Id", col, 0);
            });
        } else if (type == "Data") {
            input.addEventListener('change', (e) => {
                active_table.id_table[row][col] = e.target.value;
                render();
            });
            input.addEventListener('focus', (e) => {
                setFocus("Id", col, row+1);
            })
        }
    }
    return input;
}