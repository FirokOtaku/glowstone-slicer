// noinspection ExceptionCaughtLocallyJS

import axios from 'axios'

let token = null
export function getToken() { return token }
export function setToken(value) { token = value }
export function createAuthedForm()
{
    const form = new FormData()
    form.append('accessToken', token)
    return form
}


export async function get(config = {})
{
    try
    {
        const result = await axios(Object.assign({}, config, { method: 'get' }))
        if(result.status !== 200)
            throw result.data
        return result.data
    }
    catch (any)
    {
        throw any
    }
}

export async function post(config = {})
{
    try
    {
        const result = await axios(Object.assign({}, config, { method: 'post' }))
        if(result.status !== 200)
            throw result.data
        return result.data
    }
    catch (any)
    {
        throw any
    }
}

