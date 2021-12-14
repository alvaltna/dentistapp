import axios from 'axios';
const apiUrl = 'http://localhost:8090';
//const apiUrl = 'http://ec2-54-165-247-85.compute-1.amazonaws.com:8090';

async function getRequest(url) {
    let response = await axios.get(apiUrl + url);

    return response
}

async function getRequestWithAuthorization(url) {
    let response = await axios.get(apiUrl + url,
        {headers: {"Authorization": localStorage.getItem("Authorization")}}

        );
    return response
}

async function postRequest(url, data) {
    let response = await axios.post(apiUrl + url,
        data);

    return response
}

async function postRequestWithAuthorization(url, data) {
    let response = await axios.post(apiUrl + url,
        data,
        {headers: {"Authorization": localStorage.getItem("Authorization")}}
    );

    return response
}

async function patchRequestWithAuthorization(url, data) {
    let response = await axios.patch(apiUrl + url,
        data,
        {headers: {"Authorization": localStorage.getItem("Authorization")}}
    );

    return response
}

async function deleteRequestWithAuthorization(url) {
    let response = await axios.delete(apiUrl + url,
        {headers: {"Authorization": localStorage.getItem("Authorization")}}
    );

    return response
}

export default {
    apiUrl,
    getRequestToApi(url) {
        return getRequest(url);
    },
    getRequestToApiWithAuthorization(url) {
        return getRequestWithAuthorization(url);
    },
    postRequestToApi(url, data) {
        return postRequest(url, data);
    },
    postRequestToApiWithAuthorization(url, data) {
        return postRequestWithAuthorization(url, data);
    },
    patchRequestWithAuthorization(url, data) {
        return patchRequestWithAuthorization(url, data);
    },
    deleteRequestWithAuthorization(url, data) {
        return deleteRequestWithAuthorization(url, data);
    }
}
