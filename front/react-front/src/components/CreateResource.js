import React from 'react';
import PostRequest from '../rest/PostRequest';

const API_URL = process.env.REACT_APP_API_URL

const CreateResource = () => {
  const { formData, data, loading, error, setFormData, postData } = PostRequest(
    {
      name : '' ,
    },
    API_URL
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    postData('/resources/create');
  };

  return (
    <div>   
      <p>POST /resources/create</p>
      <form onSubmit={handleSubmit}>
        <label>
          <p>name</p>
          <input type="text" name="name" value={formData.name} onChange={handleChange} />
        </label>
        <p><button type="submit">Create</button></p>
      </form>
      <div>
        {loading && <p>Loading...</p>}
        {error && <p>{error.message}</p>}
        {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
      </div>
    </div>
  );
};

export default CreateResource;