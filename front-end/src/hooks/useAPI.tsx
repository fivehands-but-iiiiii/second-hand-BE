import { useState } from 'react';

// eslint-disable-next-line import/named
import { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';

import api from '../api';

interface APIProps {
  url: string;
  method: 'get' | 'post' | 'put' | 'delete' | 'patch';
  config?: AxiosRequestConfig;
}

const useAPI = () => {
  const [response, setResponse] = useState<AxiosResponse>();
  const [error, setError] = useState<AxiosError>();
  const [loading, setLoading] = useState(false);

  const request = async ({ url, method, config }: APIProps) => {
    setLoading(true);
    try {
      const requestConfig = {
        url,
        method,
        ...config,
      };
      const { data }: AxiosResponse = await api(requestConfig);
      setResponse(data);
      return data;
    } catch (error) {
      if (error instanceof AxiosError) {
        setError(error.response?.data || '알 수 없는 에러가 발생했습니다.');
      }
    } finally {
      setLoading(false);
    }
  };

  return { response, error, loading, request };
};

export default useAPI;
