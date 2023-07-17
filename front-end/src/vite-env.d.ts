// <reference types="vite/client" />;
interface ImportMetaEnv {
  readonly VITE_GITHUB_CLIENT_ID: string;
  readonly VITE_APP_BASE_URL: string;
  readonly VITE_APP_LOCAL_URL: string;
  readonly VITE_REDIRECT_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}