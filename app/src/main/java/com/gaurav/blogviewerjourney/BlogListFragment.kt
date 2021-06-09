package com.gaurav.blogviewerjourney

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.blogviewerjourney.adapter.BlogAdapter
import com.gaurav.blogviewerjourney.databinding.FragmentBlogListBinding
import com.gaurav.blogviewerjourney.io.model.BlogDetailModel
import com.gaurav.blogviewerjourney.io.model.BlogModel
import com.gaurav.blogviewerjourney.io.model.CommentModel
import com.gaurav.blogviewerjourney.io.model.UserModel
import com.gaurav.blogviewerjourney.io.viewmodel.BlogViewModel

class BlogListFragment : Fragment(),
    BlogAdapter.OnItemClickListener {
    private lateinit var blogViewModel: BlogViewModel
    private var binding: FragmentBlogListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blog_list, container, false)
        binding = FragmentBlogListBinding.bind(rootView)
        blogViewModel = ViewModelProvider.NewInstanceFactory().create(BlogViewModel::class.java)
        activity?.let {
            setUpViews(it)
            getBlogs()
        }

        return rootView
    }

    private fun setUpViews(activity: Activity) {
        val mLayoutManager = LinearLayoutManager(activity)
        binding?.recyclerView?.layoutManager = mLayoutManager
    }

    private fun getBlogs() {
        blogViewModel.fetchBlogList()
        blogViewModel.blogListLiveData.observe(this, {
            getUsers()
        })
    }

    private fun getUsers() {
        blogViewModel.fetchUserList()
        blogViewModel.userListLiveData.observe(this, {
            val adapter = BlogAdapter(
                prepareData(
                    blogViewModel.blogListLiveData.value,
                    blogViewModel.userListLiveData.value
                ),
                this
            )
            binding?.recyclerView?.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    private fun prepareData(
        blogList: List<BlogModel>?,
        userList: List<UserModel>?
    ): List<BlogDetailModel> {
        val list = ArrayList<BlogDetailModel>()
        blogList?.forEach { blog ->
            list.add(BlogDetailModel(
                blog,
                userList?.find { it.id == blog.userId }
            ))
        }

        return list
    }

    override fun onItemClick(blogDetailModel: BlogDetailModel) {
        Toast.makeText(
            activity, "You selected " + blogDetailModel.blogModel?.title + "!",
            Toast.LENGTH_SHORT
        ).show()

        getBlogComments(blogDetailModel)
    }

    private fun getBlogComments(blogDetailModel: BlogDetailModel): List<CommentModel> {
        val act: FragmentActivity = activity ?: return arrayListOf()

        blogDetailModel.blogModel?.id?.let {
            blogViewModel.fetchCommentListByPostId(it)
            blogViewModel.commentListLiveData.observe(this, {
                val detailsFragment =
                    BlogDetailsFragment.newInstance(
                        blogDetailModel,
                        blogViewModel.commentListLiveData.value
                    )
                act.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, detailsFragment, "blogDetails")
                    .addToBackStack(null)
                    .commit()
            })
        }

        return arrayListOf()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}