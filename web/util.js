'use strict'

let tab_counter = 1;

function createSelect(values) {
    let select = document.createElement("select");
    for (let key in values) {
        let option = document.createElement("option");
        option.value = key;
        option.innerHTML = values[key];
        select.appendChild(option);
    }
    return select;
}

function getTabCounter() {
    let tab_value = tab_counter;
    tab_counter += 1;
    return tab_value;
}

function resetTabCounter() {
    tab_counter = 1;
}