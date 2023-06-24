import { useState } from 'react';

// eslint-disable-next-line import/named
import { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';

import api from '../api';

interface APIProps {
  url: string;
  method: 'get' | 'post' | 'put' | 'delete' | 'patch';
  config?: AxiosRequestConfig;
}

const useAPI = ({ url, method, config }: APIProps) => {
  const [response, setResponse] = useState<AxiosResponse>();
  const [error, setError] = useState<AxiosError>();
  const [loading, setLoading] = useState(false);

  const request = async () => {
    setLoading(true);
    try {
      const requestConfig = {
        url,
        method,
        ...config,
      };
      const res: AxiosResponse = await api(requestConfig);
      setResponse(res.data);
    } catch (err) {
      if (err instanceof AxiosError) {
        if (err.response?.status === 401) {
          setError(err.response.data || '알 수 없는 에러가 발생했습니다.');
        } else setError(err);
      }
    } finally {
      setLoading(false);
    }
  };

  return { response, error, loading, request };
};

export default useAPI;
