import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 20,
    duration: '2m'
};

const BASE_URL = 'http://localhost:8080/api/orders';

export default function () {

    const payload = JSON.stringify({
        productName: 'Laptop',
        quantity: 1,
        price: 50000
    });

    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    // CREATE
    let createRes = http.post(BASE_URL, payload, params);

    let id = 1;
    try {
        id = JSON.parse(createRes.body).id;
    } catch (e) {}

    // READ
    http.get(`${BASE_URL}/${id}`);

    // LIST
    http.get(BASE_URL);

    // UPDATE
    http.put(`${BASE_URL}/${id}`, payload, params);

    // SLOW
    http.get(`${BASE_URL}/test/slow`);

    // ERROR
    http.get(`${BASE_URL}/test/error`);

    // DELETE
    http.del(`${BASE_URL}/${id}`);

    sleep(1);
}