export const ViteConfig = {
  /* BE BASE URL */
  VITE_BASE_URL: import.meta.env.VITE_BASE_URL,

  /* AUTH */
  VITE_EXPIRED_TIME: import.meta.env.VITE_EXPIRED_TIME,
  VITE_NAVER_CLIENT_ID: import.meta.env.VITE_NAVER_CLIENT_ID,
  VITE_NAVER_SECRET: import.meta.env.VITE_NAVER_SECRET,
  VITE_NAVER_REDIRECT_URI: import.meta.env.VITE_NAVER_REDIRECT_URI,
  VITE_NAVER_STATE: import.meta.env.VITE_NAVER_STATE,

  /* MAP API */
  VITE_KAKAO_MAP_API_KEY: import.meta.env.VITE_KAKAO_MAP_API_KEY,
  VITE_NAVER_MAP_API_KEY: import.meta.env.VITE_NAVER_MAP_API_KEY,
  VITE_NAVER_MAP_API_KEY_ID: import.meta.env.ViTE_NAVER_MAP_API_KEY_ID,
  VITE_GOOGLE_MAP_API_KEY: import.meta.env.VITE_GOOGLE_MAP_API_KEY,
  VITE_GOOGLE_MAP_API_KEY_ID: import.meta.env.VITE_GOOGLE_MAP_API_KEY_ID,

  /* WEB PUSH NOTIFICATION */
  VITE_FIREBASE_API_KEY: import.meta.env.VITE_FIREBASE_API_KEY,
  VITE_FIREBASE_AUTH_DOMAIN: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  VITE_FIREBASE_PROJECT_ID: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  VITE_FIREBASE_STORAGE_BUCKET: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET,
  VITE_FIREBASE_MESSAGING_SENDER_ID: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID,
  VITE_FIREBASE_APP_ID: import.meta.env.VITE_FIREBASE_APP_ID,
  VITE_FIREBASE_MEASUREMENT_ID: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID,
  VITE_FIREBASE_PUBLIC_VAPID_KEY: import.meta.env.VITE_FIREBASE_PUBLIC_VAPID_KEY,

  /* STOMP, SOCK */
  VITE_SOCK_URL: import.meta.env.VITE_SOCK_URL,
};
