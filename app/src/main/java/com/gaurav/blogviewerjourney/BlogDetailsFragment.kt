package com.gaurav.blogviewerjourney

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gaurav.blogviewerjourney.databinding.FragmentBlogDetailsBinding
import com.gaurav.blogviewerjourney.io.model.BlogDetailModel
import com.gaurav.blogviewerjourney.io.model.BlogModel
import com.gaurav.blogviewerjourney.io.model.CommentModel
import com.gaurav.blogviewerjourney.io.model.UserModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BlogDetailsFragment : Fragment() {
    private var binding: FragmentBlogDetailsBinding? = null
    private lateinit var mainLooper: Looper

    companion object {
        private const val BLOG_MODEL = "model"
        private const val COMMENT_LIST = "comments"
        fun newInstance(
            blogModel: BlogDetailModel,
            comments: List<CommentModel>?
        ): BlogDetailsFragment {
            val args = Bundle()
            args.putParcelable(BLOG_MODEL, blogModel)
            args.putParcelableArrayList(COMMENT_LIST, comments as ArrayList<CommentModel>)
            val fragment = BlogDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blog_details, container, false)
        binding = FragmentBlogDetailsBinding.bind(rootView)

        mainLooper = Looper.getMainLooper()
        GlobalScope.launch {
            displayData()
        }
        return rootView
    }

    private fun displayData() {
        val displayText: String?
        val model: BlogDetailModel? = arguments?.getParcelable(BLOG_MODEL)
        val comments: ArrayList<CommentModel>? = arguments?.getParcelableArrayList(COMMENT_LIST)
        if (model != null) {
            val blogModel: BlogModel? = model.blogModel
            val userModel: UserModel? = model.userModel
            if (blogModel != null) {
                val sb = StringBuilder()
                sb.append("Blog Details\n\n")
                sb.append("Id - " + blogModel.id + "\n")
                sb.append("Title - " + blogModel.title + "\n")
                sb.append("Body - " + blogModel.body + "\n\n")
                sb.append("User Id - " + blogModel.userId + "\n")

                userModel?.let {
                    sb.append("User Name - " + it.name + "\n")
                    sb.append("Email - " + it.email + "\n\n")
                }

                comments?.let {
                    sb.append("Comments:\n")
                    var i = 0
                    it.forEach { comment ->
                        sb.append("(" + (++i) + ") - " + comment.body + "\n")
                    }
                }

                displayText = sb.toString()
            } else {
                displayText = "Sorry. Data could not be loaded."
            }
        } else {
            displayText = "Sorry. Data could not be loaded."
        }

        Handler(mainLooper).post {
            binding?.detailsTextView?.text = displayText
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}