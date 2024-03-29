import React from 'react';
import PostRequest from '../rest/PostRequest';

const API_URL = process.env.REACT_APP_API_URL

const Signup = () => {
  const { formData, data, loading, error, setFormData, postData } = PostRequest(
    {
      name : '' ,
      password: '',
    },
    API_URL
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    postData('/signup');
  };

  return (
    <div>
      <p>Регистрация</p>
      <p>POST /signup</p>
      <form onSubmit={handleSubmit}>
        <label>
          <p>login</p>
          <input type="text" name="name" value={formData.name} onChange={handleChange} />
        </label>
        <label>
          <p>password</p>
          <input type="password" name="password" value={formData.password} onChange={handleChange} />
        </label>
        <p><button type="submit">Signup</button></p>
      </form>
      <div>
        {loading && <p>Loading...</p>}
        {error && <p>{error.message}</p>}
        {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
      </div>
    </div>
  );
};

export default Signup;