interface sessionStorageProps {
  key: string;
  value?: object;
}

const getStoredValue = ({ key }: sessionStorageProps) => {
  try {
    const value = sessionStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  } catch (err) {
    console.error('Error getting sessionStorage:', err);
  }
};

const setStorageValue = ({ key, value }: sessionStorageProps) => {
  try {
    sessionStorage.setItem(key, JSON.stringify(value));
  } catch (err) {
    console.error('Error setting sessionStorage:', err);
  }
};

const removeStorageValue = ({ key }: sessionStorageProps) => {
  try {
    sessionStorage.removeItem(key);
  } catch (err) {
    console.error('Error removing sessionStorage:', err);
  }
};

export { getStoredValue, setStorageValue, removeStorageValue };
