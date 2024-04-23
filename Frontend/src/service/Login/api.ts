export const login = async (email: string, password: string) => {
  const response = await fetch(`${import.meta.env.VITE_BASE_URL}/엔드포인트`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });

  return response.json();
}